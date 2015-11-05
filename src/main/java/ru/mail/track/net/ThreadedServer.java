package ru.mail.track.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

///////////import org.slf4j.Logger;
///////////import org.slf4j.LoggerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mail.track.message.messagetypes.Message;
import ru.mail.track.message.CommandHandler;
import ru.mail.track.message.Result;
import ru.mail.track.message.messagetypes.SimpleMessage;
import ru.mail.track.message.User;
import ru.mail.track.message.UserStorage;
import ru.mail.track.session.Session;

/**
 *
 */
public class ThreadedServer implements MessageListener {

    /////////////////static Logger log = LoggerFactory.getLogger(ThreadedServer.class);

    public static final int PORT = 19000;
    private volatile boolean isRunning;
    private Map<Long, ConnectionHandler> handlers = new HashMap<>();
    private ServerSocket sSocket;
    private UserStorage store;
    private CommandHandler commandHandler;
    private Map<Long, User> userMap = new TreeMap<>();
    private SessionManager sessionManager = new SessionManager();
    static Logger log = LoggerFactory.getLogger(ThreadedServer.class);

    public ThreadedServer(UserStorage store, CommandHandler commandHandler) {
        this.store = store;
        this.commandHandler = commandHandler;

        try {
            sSocket = new ServerSocket(PORT);
            sSocket.setReuseAddress(true);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public void startServer() throws Exception {
        //////////////log.info("Started, waiting for connection");
        System.out.println("Started, waiting for connection");

        isRunning = true;
        while (isRunning) {
            Socket socket = sSocket.accept();
            //////////////log.info("Accepted. " + socket.getInetAddress());
            System.out.println("Accepted" + socket.getInetAddress());
            ConnectionHandler handler = new SocketConnectionHandler(socket);
            handler.addListener(this);

            Session session = sessionManager.createSession();
            session.setSessionManager(sessionManager);
            session.setConnectionHandler(handler);
            handlers.put(session.getId(), handler);
            handler.setID(session.getId());
            Thread thread = new Thread(handler);
            thread.start();
        }
    }

    public void stopServer() {
        isRunning = false;
        for (ConnectionHandler handler : handlers.values()) {
            handler.stop();
        }
    }

    @Override
    public void onMessage(Message message, long id) {

        log.info("THREADED SERVER:onMessage: {}", message);

        Result result = commandHandler.work(message, sessionManager.getSession(id));
        log.info("THREADED SERVER:onMessage, result: {}", result.toString());
        try {
            sessionManager.getSession(id).send(new SimpleMessage(result.toString()));
        } catch (Exception exc) {
            exc.printStackTrace();
            System.err.println("some troubles while sending message to session " + id);
        }
    }


}
