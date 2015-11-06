package ru.mail.track.message.messagetypes;

import ru.mail.track.perform.CommandType;

/**
 * Created by aliakseisemchankau on 6.11.15.
 */
public class ChatHistoryMessage extends Message {

    private boolean hasArg = false;
    private Long countOfMessages;
    private Long chatId;

    public ChatHistoryMessage() {
        setType(CommandType.CHAT_HISTORY);
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }



    public boolean isHasArg() {
        return hasArg;
    }

    public void setHasArg(boolean hasArg) {
        this.hasArg = hasArg;
    }

    public Long getCountOfMessages() {
        return countOfMessages;
    }

    public void setCountOfMessages(Long countOfMessages) {
        this.countOfMessages = countOfMessages;
    }
}
