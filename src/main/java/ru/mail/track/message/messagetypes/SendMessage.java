package ru.mail.track.message.messagetypes;

import ru.mail.track.perform.CommandType;

/**
 * Created by aliakseisemchankau on 3.11.15.
 */
public class SendMessage extends Message {

    private Long chatId;

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public SendMessage() {
        setType(CommandType.MSG_SEND);
    }

    public SendMessage(String text) {
        setType(CommandType.MSG_SEND);
        setMessage(text);
    }

}
