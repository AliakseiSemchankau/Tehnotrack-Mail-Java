package ru.mail.track.message.messagetypes;

import ru.mail.track.perform.CommandType;

/**
 * Created by aliakseisemchankau on 4.11.15.
 */
public class SimpleMessage extends Message {

    public SimpleMessage() {
        setType(CommandType.SIMPLE_MSG);
    }

    public SimpleMessage(final String msg) {
        setType(CommandType.SIMPLE_MSG);
        setMessage(msg);
    }

}
