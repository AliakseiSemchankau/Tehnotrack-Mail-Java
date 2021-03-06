package ru.mail.track.net.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

////////import org.slf4j.Logger;
////////import org.slf4j.LoggerFactory;

import ru.mail.track.message.messagetypes.Message;
import ru.mail.track.net.protocol.Protocol;
import ru.mail.track.net.protocol.SerializableProtocol;

/**
 * Класс работающий с сокетом, умеет отправлять данные в сокет
 * Также слушает сокет и рассылает событие о сообщении всем подписчикам (асинхронность)
 */
public class SocketConnectionHandler implements ConnectionHandler {

    //static Logger log = LoggerFactory.getLogger(SocketConnectionHandler.class);

    // подписчики
    private List<MessageListener> listeners = new ArrayList<>();
    private Socket socket;
    private InputStream in;
    private OutputStream out;
    private Protocol protocol = new SerializableProtocol();
    private long uniqueID;

    public SocketConnectionHandler(Socket socket) throws IOException {
        this.socket = socket;
        in = socket.getInputStream();
        out = socket.getOutputStream();
    }

    @Override
    public void send(Message msg) throws IOException {

        //System.out.println("SCH ID=" + Thread.currentThread().getId());
        //log.info("SOCKET CONNECTION HANDLER SEND: {}", msg);

        // TODO: здесь должен быть встроен алгоритм кодирования/декодирования сообщений
        // то есть требуется описать протокол
        out.write(protocol.encode(msg));
        out.flush();
    }

    // Добавить еще подписчика
    @Override
    public void addListener(MessageListener listener) {
        listeners.add(listener);
    }


    // Разослать всем
    public void notifyListeners(Message msg) {
        listeners.forEach(it -> it.onMessage(msg, uniqueID));
    }

    @Override
    public void run() {
        final byte[] buf = new byte[1024 * 64];
        while (!Thread.currentThread().isInterrupted()) {
            try {
                int read = in.read(buf, 0, in.available());
                if (read > 0) {
                    Message msg = protocol.decode(Arrays.copyOf(buf, read));

                    //log.info("SOCKET CONNECTION HANDLER: message received: {}", msg);

                    // Уведомим всех подписчиков этого события
                    notifyListeners(msg);
                }
            } catch (Exception e) {
                //////////////log.error("Failed to handle connection: {}", e);
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
        //System.out.println("RUN ID=" + Thread.currentThread().getId());
    }

    @Override
    public void stop() {
        //System.out.println("STOP ID=" + Thread.currentThread().getId());
        Thread.currentThread().interrupt();
    }

    @Override
    public void setID(long id) {
        uniqueID = id;
    }

    @Override
    public long getID() {
        return uniqueID;
    }

}
