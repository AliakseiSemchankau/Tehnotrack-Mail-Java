package ru.mail.track.message.messagetypes;

import ru.mail.track.perform.CommandType;

/**
 * Created by aliakseisemchankau on 4.11.15.
 */
public class ChatFindMessage extends Message {

    private String pattern;
    private Long chatId;

    public ChatFindMessage() {
        setType(CommandType.CHAT_FIND);
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
}
