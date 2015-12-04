package ru.mail.track.net.io;

import ru.mail.track.message.messagetypes.Message;

/**
 * Слушает сообщения
 */
public interface MessageListener {

    void onMessage(Message message, long id);
}



