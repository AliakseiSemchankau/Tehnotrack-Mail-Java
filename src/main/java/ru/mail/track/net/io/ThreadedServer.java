package ru.mail.track.net.io;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

///////////import org.slf4j.Logger;
///////////import org.slf4j.LoggerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mail.track.message.messagetypes.Message;
import ru.mail.track.message.CommandHandler;
import ru.mail.track.message.Result;
import ru.mail.track.message.messagetypes.SimpleMessage;
import ru.mail.track.net.Server;
import ru.mail.track.net.SessionManager;
import ru.mail.track.session.Session;

/**
 *
 */
public class ThreadedServer implements MessageListener, Server {

    static Logger log = LoggerFactory.getLogger(ThreadedServer.class);

    public static final int PORT = 19001;
    private volatile boolean isRunning;
    private Map<Long, ConnectionHandler> handlers = new HashMap<>();
    private Map<Long, Thread> threadHandlers = new HashMap<>();
    private ServerSocket sSocket;
    private CommandHandler commandHandler;
    private SessionManager sessionManager = new SessionManager();
    //static Logger log = LoggerFactory.getLogger(ThreadedServer.class);

    public ThreadedServer(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;

        try {
            sSocket = new ServerSocket(PORT);
            sSocket.setReuseAddress(true);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    @Override
    public void send(Message msg, Long sessionId) {

        if (handlers.containsKey(sessionId)) {
            ConnectionHandler handler = handlers.get(sessionId);
            try {
                handler.send(msg);
            } catch (IOException ioExc) {
                System.err.println("couldn't send message for sessionId=" + sessionId);
                ioExc.printStackTrace();
            }
        }

    }

    @Override
    public void startServer() {
        //////////////log.info("Started, waiting for connection");
        System.out.println("Started, waiting for connection");

        isRunning = true;
        while (isRunning) {
            Socket socket = null;
            try {
                socket = sSocket.accept();
            } catch (IOException ioExc) {
                System.err.println("THREADED SERVER::START SERVER: failed to accept sSocket");
                continue;
            }
            //////////////log.info("Accepted. " + socket.getInetAddress());
            System.out.println("Accepted" + socket.getInetAddress());
            try {
                ConnectionHandler handler = new SocketConnectionHandler(socket);
                handler.addListener(this);

                Session session = sessionManager.createSession();
                session.setServer(this);
                session.setSessionManager(sessionManager);
                //session.setConnectionHandler(handler);

                threadHandlers.put(session.getId(), new Thread(handler));
                handlers.put(session.getId(), handler);
                handler.setID(session.getId());
                threadHandlers.get(session.getId()).start();
            } catch (IOException ioExc) {
                System.err.println("THREADED SERVER::START SERVER : failed to crate " +
                        "handler for socket with address " +
                        socket.getInetAddress());
                ioExc.printStackTrace();
            }
        }

    }

    @Override
    public void destroyServer() {
        isRunning = false;
        System.out.println("done!");
        try {
            sSocket.close();
        } catch (IOException ioExc) {
            System.err.println("THREADED SERVER:DESTROY SERVER: failed to close sSocket");
            ioExc.printStackTrace();
        }
        System.out.println("closing handlers");
        for (Thread handler : threadHandlers.values()) {
            handler.interrupt();
        }
        System.out.println("handlers closed successfully");
        commandHandler.close();
        System.out.println("commandHandler closed succesfully");
    }

    @Override
    public void onMessage(Message message, long id) {

        //log.info("THREADED SERVER:onMessage: {}", message);

        Result result = commandHandler.work(message, sessionManager.getSession(id));
        //log.info("THREADED SERVER:onMessage, result: {}", result.toString());
        try {
            sessionManager.getSession(id).send(new SimpleMessage(result.toString()));
        } catch (Exception exc) {
            exc.printStackTrace();
            System.err.println("some troubles while sending message to session " + id);
        }
    }


}
