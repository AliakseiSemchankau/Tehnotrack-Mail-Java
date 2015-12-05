package ru.mail.track.net.nio;

import ru.mail.track.message.messagetypes.ChatCreateMessage;
import ru.mail.track.message.messagetypes.ChatFindMessage;
import ru.mail.track.message.messagetypes.ChatHistoryMessage;
import ru.mail.track.message.messagetypes.ChatListMessage;
import ru.mail.track.message.messagetypes.ChatSendMessage;
import ru.mail.track.message.messagetypes.HelpMessage;
import ru.mail.track.message.messagetypes.InfoMessage;
import ru.mail.track.message.messagetypes.LoginMessage;
import ru.mail.track.message.messagetypes.Message;
import ru.mail.track.message.messagetypes.PassMessage;
import ru.mail.track.message.messagetypes.RegisterMessage;
import ru.mail.track.message.messagetypes.SimpleMessage;
import ru.mail.track.net.protocol.Protocol;
import ru.mail.track.net.protocol.SerializableProtocol;
import ru.mail.track.perform.CommandType;
import sun.security.util.Length;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


/**
 *
 */
public class NioClient {

    Protocol protocol = new SerializableProtocol();

    //static Logger log = LoggerFactory.getLogger(NioClient.class);


    public static final int PORT = 19002;

    private Selector selector;
    private SocketChannel channel;
    private ByteBuffer buffer = ByteBuffer.allocate(256);
    private boolean flagInterrupt = false;
    List<Byte> data = new ArrayList<>();

    BlockingQueue<String> queue = new ArrayBlockingQueue<>(2);

    // TODO: Нужно создать блокирующую очередь, в которую складывать данные для обмена между потоками

    private Message result;

    public Message getResult() {
        return result;
    }

    public void addLineInQueue(String line) {
        synchronized (queue) {
            try {
                //System.err.println("line=" + line);
                queue.put(line);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Будим селектор
        SelectionKey key = channel.keyFor(selector);
        System.out.println("wake up: " + key.hashCode());
        key.interestOps(SelectionKey.OP_WRITE);
        selector.wakeup();

    }

    public void init() {

        // Слушаем ввод данных с консоли
        Thread t = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (!Thread.currentThread().isInterrupted()) {
                String line = scanner.nextLine();
                if ("q".equals(line)) {
                    flagInterrupt = true;

                    try {
                        selector.close();
                    } catch (IOException ioExc) {
                        System.err.println("couldn't close selector");
                        ioExc.printStackTrace();
                    }
                    try {
                        channel.close();
                    } catch (IOException ioExc) {
                        System.err.println("couldn't close channel");
                        ioExc.printStackTrace();
                    }

                    Thread.currentThread().interrupt();
                    System.out.println("Exit!");
                }

                if (Thread.currentThread().isInterrupted()) {
                    break;
                }

                // TODO: здесь нужно сложить прочитанные данные в очередь

                addLineInQueue(line);


            }
        });
        t.start();


        try {
            selector = Selector.open();
            channel = SocketChannel.open();
            channel.configureBlocking(false);
            channel.register(selector, SelectionKey.OP_CONNECT);

            channel.connect(new InetSocketAddress("localhost", PORT));
        } catch (Exception exc) {
            System.err.println("mistake with selector or channel");
            exc.printStackTrace();
        }

        while (!flagInterrupt) {
            //System.err.println("we're doing smth");
            // System.out.println("Waiting on select()...");
            try {
                selector.select();
            } catch (Exception exc) {
                System.err.println("couldn't make selector.select()");
                exc.printStackTrace();
            }
            //System.err.println("kek");
            // System.out.println("Raised {} events" + num);


            if (flagInterrupt) {
                break;
            }
            //System.err.println("kek2");
            Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
            //System.err.println("kek3");
            while (keyIterator.hasNext()) {


                SelectionKey sKey = keyIterator.next();
                keyIterator.remove();

                if (sKey.isConnectable()) {
                    System.out.println("[connectable] {}" + sKey.hashCode());
                    connect(sKey);
                } else if (sKey.isReadable()) {
                    System.out.println("[readable]");
                    read(sKey);
                } else if (sKey.isWritable()) {
                    System.out.println("[writable]");
                    write(sKey);
                }
            }

        }
    }

    private void connect(SelectionKey sKey) {
        try {
            channel.finishConnect();
        } catch (IOException ioExc) {
            System.err.println("couldn't finish connection for channel " + channel.toString());
            ioExc.printStackTrace();
        }
        // теперь в канал можно писать
        sKey.interestOps(SelectionKey.OP_WRITE);
    }

    private boolean isDownloading = false;
    private long length = 0;

    private void read(SelectionKey sKey) {

        buffer.clear();
        int numRead = -1;
        try {
            numRead = channel.read(buffer);
        } catch (IOException ioExc) {
            System.err.println("couldn't read buffer from channel " + channel.toString());
        }
        buffer.flip();

        if (numRead < 0)
            //System.err.println("break, lol");
            return;

        System.out.println("client red: " + numRead);

        //System.out.println("From server: {}" + new String(buffer.array()));
        //buffer.clear();
        //buffer.fl


        for (int i = 0; i < numRead; ++i) {
            data.add(buffer.array()[i]);
        }

        if (!isDownloading && data.size() >= 8) {
            length = Packer.defineLength(data);
            isDownloading = true;
        }

        if (data.size() >= length + 8) {
            byte[] byteData = new byte[data.size()];
            for (int i = 0; i < data.size(); ++i) {
                byteData[i] = data.get(i);
            }

            Message msg = protocol.decode(Packer.unpack(byteData));
            if (msg instanceof ChatSendMessage) {
                ChatSendMessage chmsg = (ChatSendMessage) msg;
                System.out.println();
                System.out.println("chat id=" + chmsg.getChatId());
                System.out.println("sender id=" + chmsg.getSender());
                System.out.println("timestamp=" + chmsg.getTimeStamp());
                System.out.println("message=" + chmsg.getMessage());
                System.out.println();
            } else {
                System.out.printf("\nNIOCLIENT:\n %s\n", msg.getMessage());
            }
            result = msg;
            data = new ArrayList<>();
            isDownloading = false;
        }

        //sKey.cancel();
        //channel.close();

    }

    private void write(SelectionKey sKey) {
        //System.out.println("[writable]");

        //TODO: здесь нужно вытащить данные из очереди и отдать их на сервер

        //byte[] userInput =
        //channel.write(ByteBuffer.wrap(userInput));

        synchronized (queue) {

            while (!queue.isEmpty()) {

                // System.out.println(queue.size() + "=queue.size()");
                String line = queue.poll();
                //System.out.println(queue.size() + "=queue.size() now");

                try {
                    Message msg = processInput(line);
                    if (msg != null) {
                        try {
                            //System.err.println("CLIENT::WRITE::" + msg.toString());
                            channel.write(ByteBuffer.wrap(protocol.encode(msg)));
                        } catch (IOException ioExc) {
                            System.err.println("couldn't write message " + msg + " to channel " + channel.toString());
                        }
                    }
                } catch (IllegalArgumentException iaExc) {
                    System.err.println("illegal input=" + line);
                }
            }
        }
        // Ждем записи в канал
        sKey.interestOps(SelectionKey.OP_READ);
    }

    public static void main(String[] args) {
        NioClient client = new NioClient();
        client.init();
    }

    public Message processInput(String line) throws IllegalArgumentException {

        String[] tokens = line.split(" ");
        //log.info("Tokens: {}", Arrays.toString(tokens));
        String cmdType = tokens[0];


        if ("\\login".equals(cmdType)) {
            LoginMessage loginMessage = new LoginMessage();
            loginMessage.setType(CommandType.USER_LOGIN);
            loginMessage.setLogin(tokens[1]);
            loginMessage.setPassword(tokens[2]);
            return loginMessage;
        }

       /* if ("\\send".equals(cmdType)) {
            ChatSendMessage chatSendMessage = new ChatSendMessage();
            chatSendMessage.setType(CommandType.CHAT_SEND);
            chatSendMessage.setChatId(Long.valueOf(tokens[1]));
            chatSendMessage.setMessage(tokens[2]);
            handler.send(chatSendMessage);
            return;
        }*/

        if ("\\simple".equals(cmdType)) {
            SimpleMessage simpleMessage = new SimpleMessage();
            simpleMessage.setType(CommandType.SIMPLE_MSG);
            simpleMessage.setMessage(tokens[1]);
            return simpleMessage;
        }

        if ("\\register".equals(cmdType)) {
            RegisterMessage registerMessage = new RegisterMessage();
            registerMessage.setType(CommandType.USER_REGISTER);
            registerMessage.setLogin(tokens[1]);
            registerMessage.setPassword(tokens[2]);
            return registerMessage;
        }

        if ("\\help".equals(cmdType)) {
            HelpMessage helpMessage = new HelpMessage();
            helpMessage.setType(CommandType.USER_HELP);
            return helpMessage;
        }

        if ("\\user_info".equals(cmdType)) {
            InfoMessage infoMessage = new InfoMessage();
            infoMessage.setType(CommandType.USER_INFO);
            if (tokens.length > 1) {
                infoMessage.setHasArg(true);
                infoMessage.setUserId(Long.valueOf(tokens[1]));
            }
            return infoMessage;
        }

        if ("\\user_pass".equals(cmdType)) {
            PassMessage passMessage = new PassMessage();
            passMessage.setType(CommandType.USER_PASS);
            passMessage.setOldPass(tokens[1]);
            passMessage.setNewPass(tokens[2]);
            return passMessage;
        }

        if ("\\chat_list".equals(cmdType)) {
            ChatListMessage chatListMessage = new ChatListMessage();
            chatListMessage.setType(CommandType.CHAT_LIST);
            return chatListMessage;
        }

        if ("\\chat_create".equals(cmdType)) {
            ChatCreateMessage chatCreateMessage = new ChatCreateMessage();
            chatCreateMessage.setType(CommandType.CHAT_CREATE);
            for (int i = 1; i < tokens.length; ++i) {
                String token = tokens[i];
                chatCreateMessage.addId(Long.valueOf(token));
            }
            return chatCreateMessage;
        }

        if ("\\chat_history".equals(cmdType)) {
            ChatHistoryMessage chatHistoryMessage = new ChatHistoryMessage();
            chatHistoryMessage.setType(CommandType.CHAT_HISTORY);
            chatHistoryMessage.setChatId(Long.valueOf(tokens[1]));
            if (tokens.length > 2) {
                chatHistoryMessage.setHasArg(true);
                chatHistoryMessage.setCountOfMessages(Long.valueOf(tokens[2]));
            }
            return chatHistoryMessage;
        }

        if ("\\chat_find".equals(cmdType)) {
            ChatFindMessage chatFindMessage = new ChatFindMessage();
            chatFindMessage.setType(CommandType.CHAT_FIND);
            chatFindMessage.setChatId(Long.valueOf(tokens[1]));
            chatFindMessage.setPattern(tokens[2]);
            return chatFindMessage;
        }

        if ("\\chat_send".equals(cmdType)) {
            ChatSendMessage chatSendMessage = new ChatSendMessage();
            chatSendMessage.setType(CommandType.CHAT_SEND);
            chatSendMessage.setChatId(Long.valueOf(tokens[1]));
            StringBuilder textMsg = new StringBuilder();
            for (int i = 2; i < tokens.length; ++i) {
                textMsg.append(tokens[i] + " ");
            }
            chatSendMessage.setMessage(textMsg.toString());
            return chatSendMessage;
        }

        throw new IllegalArgumentException("input is incorrect:" + line);

    }


}