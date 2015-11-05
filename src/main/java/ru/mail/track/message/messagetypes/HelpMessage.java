package ru.mail.track.message.messagetypes;

import ru.mail.track.perform.CommandType;

/**
 * Created by aliakseisemchankau on 4.11.15.
 */
public class HelpMessage extends Message {

    public HelpMessage() {
        setType(CommandType.USER_HELP);
    };

}
