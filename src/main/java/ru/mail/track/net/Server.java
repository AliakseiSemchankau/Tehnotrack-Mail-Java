package ru.mail.track.net;

import ru.mail.track.message.messagetypes.Message;

/**
 * Created by aliakseisemchankau on 28.11.15.
 */
public interface Server {

    void send(Message msg, Long sessionId);

    void startServer();

    void destroyServer();
}

