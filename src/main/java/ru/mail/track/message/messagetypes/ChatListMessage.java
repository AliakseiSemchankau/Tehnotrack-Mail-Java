package ru.mail.track.message.messagetypes;

import ru.mail.track.perform.CommandType;

/**
 * Created by aliakseisemchankau on 6.11.15.
 */
public class ChatListMessage extends Message {

    public ChatListMessage() {
        setType(CommandType.CHAT_LIST);
    }

}
