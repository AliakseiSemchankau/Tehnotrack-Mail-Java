package ru.mail.track.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;

///////////import org.slf4j.Logger;
///////////import org.slf4j.LoggerFactory;

import ru.mail.track.message.Message;
import ru.mail.track.message.MessageWorker;
import ru.mail.track.message.Result;
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
    private MessageWorker messageWorker;
    private Map<Long, User> userMap = new TreeMap<>();
    private SessionManager sessionManager = new SessionManager();

    public ThreadedServer(UserStorage store, MessageWorker messageWorker) {
        this.store = store;
        this.messageWorker = messageWorker;

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

        /////////////log.info("onMessage: {}", message);
        System.out.println(message);

        Result result = messageWorker.work(message, sessionManager.getSession(id));

    }


}
