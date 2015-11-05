package ru.mail.track.message;

import ru.mail.track.message.messagetypes.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 */
public class MessageStoreStub implements MessageStore {

    public static final AtomicLong counter = new AtomicLong(0);

    Map<Long, Message> messages = new HashMap<>();

    static Map<Long, Chat> chats = new HashMap<>();

    @Override
    public List<Long> getChatsByUserId(Long userId) {
        return null;
    }

    @Override
    public Chat getChatById(Long chatId) {
        return chats.get(chatId);
    }

    @Override
    public List<Long> getMessagesFromChat(Long chatId) {
        return chats.get(chatId).getMessageIds();
    }

    @Override
    public Message getMessageById(Long messageId) {
        return messages.get(messageId);
    }

    @Override
    public void addMessage(Long chatId, Message message) {
        message.setMessageId(counter.getAndIncrement());
        chats.get(chatId).addMessage(message.getMessageId());
        messages.put(message.getMessageId(), message);
    }

    @Override
    public void addUserToChat(Long userId, Long chatId) {

    }
}