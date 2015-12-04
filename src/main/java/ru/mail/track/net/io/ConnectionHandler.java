package ru.mail.track.net.io;

import java.io.IOException;

import ru.mail.track.message.messagetypes.Message;

/**
 * Обработчик сокета
 */
public interface ConnectionHandler extends Runnable {

    void send(Message msg) throws IOException;

    void addListener(MessageListener listener);

    void stop();

    void setID(long id);

    long getID();
}