package ru.mail.track.net;

import ru.mail.track.message.messagetypes.Message;

/**
 *
 */
public interface Protocol {

    public Message decode(byte[] bytes);

    public byte[] encode(Message msg);

}