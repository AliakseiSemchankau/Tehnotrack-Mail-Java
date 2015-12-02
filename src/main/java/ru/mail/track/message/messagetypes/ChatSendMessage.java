package ru.mail.track.message.messagetypes;

import ru.mail.track.perform.CommandType;

/**
 * Created by aliakseisemchankau on 3.11.15.
 */
public class ChatSendMessage extends Message {

    private Long chatId;

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public ChatSendMessage() {
        setType(CommandType.CHAT_SEND);
    }

    public ChatSendMessage(Long chatId, String text) {
        this.chatId = chatId;
        setType(CommandType.CHAT_SEND);
        setMessage(text);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChatSendMessage that = (ChatSendMessage) o;

        return !(chatId != null ? !chatId.equals(that.chatId) : that.chatId != null);

    }

    @Override
    public int hashCode() {
        return chatId != null ? chatId.hashCode() : 0;
    }
}
