package ru.mail.track.net.nio;

import ru.mail.track.message.CommandHandler;
import ru.mail.track.message.Result;
import ru.mail.track.message.messagetypes.Message;
import ru.mail.track.message.messagetypes.SimpleMessage;
import ru.mail.track.net.Server;
import ru.mail.track.net.SessionManager;
import ru.mail.track.net.protocol.Protocol;
import ru.mail.track.net.protocol.SerializableProtocol;
import ru.mail.track.session.Session;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by aliakseisemchankau on 29.11.15.
 */
public class NioServer implements Server{

    CommandHandler commandHandler;
    SessionManager sessionManager = new SessionManager();

    Map<Long, SocketChannel> socketChannels = new HashMap<>();
    Map<SocketChannel, Long> channelSessions = new HashMap<>();
    Protocol protocol = new SerializableProtocol();

    // The host:port combination to listen on
    private final InetAddress hostAddress = null;
    private int port = 19001;

    // The channel on which we'll accept connections
    private ServerSocketChannel serverChannel;

    // The selector we'll be monitoring
    private Selector selector;

    // The buffer into which we'll read data when it's available
    private ByteBuffer readBuffer = ByteBuffer.allocate(8192);

    private EchoWorker worker;

    // A list of ChangeRequest instances
    private List changeRequests = new LinkedList();

    // Maps a SocketChannel to a list of ByteBuffer instances
    private Map pendingData = new HashMap();

    public NioServer(CommandHandler commandHandler) throws IOException {
        //this.hostAddress = hostAddress;
        //this.port = port;
        this.commandHandler = commandHandler;
        selector = initSelector();
        //this.worker = new EchoWorker();
        //this.worker.setCommandHandler(commandHandler);
        //Thread t = new Thread(worker);
        //t.start();
    }

    private Selector initSelector() throws IOException {
        // Create a new selector
        Selector socketSelector = SelectorProvider.provider().openSelector();

        // Create a new non-blocking server socket channel
        serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);

        // Bind the server socket to the specified address and port
        InetSocketAddress isa = new InetSocketAddress(this.hostAddress, this.port);
        serverChannel.socket().bind(isa);

        // Register the server socket channel, indicating an interest in
        // accepting new connections
        serverChannel.register(socketSelector, SelectionKey.OP_ACCEPT);

        return socketSelector;
    }

    public void send(SocketChannel socket, byte[] data) {
        synchronized (changeRequests) {
            // Indicate we want the interest ops set changed
            changeRequests.add(new ChangeRequest(socket, ChangeRequest.CHANGEOPS, SelectionKey.OP_WRITE));

            // And queue the data we want written
            synchronized (pendingData) {
                List queue = (List) pendingData.get(socket);
                if (queue == null) {
                    queue = new ArrayList();
                    pendingData.put(socket, queue);
                }
                queue.add(data);
                //System.out.println("queue add next:" + new String(data));
            }
        }

        // Finally, wake up our selecting thread so it can make the required changes
        this.selector.wakeup();
    }

    private void accept(SelectionKey key) throws IOException {
        // For an accept to be pending the channel must be a server socket channel.
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();

        // Accept the connection and make it non-blocking
        SocketChannel socketChannel = serverSocketChannel.accept();
        Socket socket = socketChannel.socket();
        socketChannel.configureBlocking(false);

        // Register the new SocketChannel with our Selector, indicating
        // we'd like to be notified when there's data waiting to be read
        socketChannel.register(selector, SelectionKey.OP_READ);

        Session session = sessionManager.createSession();
        session.setServer(this);
        session.setSessionManager(sessionManager);
        socketChannels.put(session.getId(), socketChannel);
        channelSessions.put(socketChannel, session.getId());

    }

    private void read(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();

        // Clear out our read buffer so it's ready for new data
        readBuffer.clear();

        // Attempt to read off the channel
        int numRead;
        try {
            numRead = socketChannel.read(readBuffer);
        } catch (IOException e) {
            // The remote forcibly closed the connection, cancel
            // the selection key and close the channel.
            key.cancel();
            socketChannel.close();
            return;
        }

        if (numRead == -1) {
            // Remote entity shut the socket down cleanly. Do the
            // same from our end and cancel the channel.
            key.channel().close();
            key.cancel();
            return;
        }

        //System.out.println("NIOSERVER::READ::we have red:" + new String(this.readBuffer.array()));

        // Hand the data off to our worker thread
        //worker.processData(this, socketChannel, this.readBuffer.array(), numRead);
        Message msg = protocol.decode(this.readBuffer.array());
        System.out.println("\nNIOSERVER::READ::" + msg + "\n");
        Long id = channelSessions.get(socketChannel);
        Result result = commandHandler.work(msg, sessionManager.getSession(id));
        send(socketChannel, protocol.encode(new SimpleMessage(result.toString())));
    }

    private void write(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();

        synchronized (pendingData) {
            List queue = (List) pendingData.get(socketChannel);

            // Write until there's not more data ...
            while (!queue.isEmpty()) {
                byte[] data = (byte[]) queue.get(0);
                ByteBuffer buf = ByteBuffer.wrap(Packer.pack(data));
                //System.out.println("we write:\n" + new String(buf.array()));
               // System.err.println("&\n");
               // for(int i  = 0; i < buf.array().length; ++i) {
               //     System.err.println(buf.array()[i]);
               // }
               // System.err.println("\n&\n");
                //System.err.println(buf.array().length + "=array.length");
                socketChannel.write(buf);
                if (buf.remaining() > 0) {
                    // ... or the socket's buffer fills up
                    break;
                }
                queue.remove(0);
            }

            if (queue.isEmpty()) {
                // We wrote away all data, so we're no longer interested
                // in writing on this socket. Switch back to waiting for
                // data.
                key.interestOps(SelectionKey.OP_READ);
            }
        }
    }


    @Override
    public void send(Message msg, Long sessionId) {
        byte[] data = protocol.encode(msg);
        SocketChannel socketChannel = socketChannels.get(sessionId);
        send(socketChannel, data);
    }

    @Override
    public void startServer() throws Exception {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                // Process any pending changes
                synchronized(changeRequests) {
                    Iterator changes = this.changeRequests.iterator();
                    while (changes.hasNext()) {
                        ChangeRequest change = (ChangeRequest) changes.next();
                        switch(change.type) {
                            case ChangeRequest.CHANGEOPS:
                                SelectionKey key = change.socket.keyFor(this.selector);
                                key.interestOps(change.ops);
                        }
                    }
                    this.changeRequests.clear();
                }

                // Wait for an event one of the registered channels
                this.selector.select();

                // Iterate over the set of keys for which events are available
                Iterator selectedKeys = this.selector.selectedKeys().iterator();
                while (selectedKeys.hasNext()) {
                    SelectionKey key = (SelectionKey) selectedKeys.next();
                    selectedKeys.remove();

                    if (!key.isValid()) {
                        continue;
                    }

                    // Check what event is available and deal with it
                    if (key.isAcceptable()) {
                        System.out.println("accept key=" + key.hashCode());
                        accept(key);
                    } else if (key.isReadable()) {
                        System.out.println("read key=" + key.hashCode());
                        read(key);
                    } else if (key.isWritable()) {
                        System.out.println("write key=" + key.hashCode());
                        write(key);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void destroyServer() throws Exception {
        serverChannel.close();
        for(SocketChannel socketChannel : socketChannels.values()) {
            socketChannel.close();
            selector.close();
        }
    }



}
