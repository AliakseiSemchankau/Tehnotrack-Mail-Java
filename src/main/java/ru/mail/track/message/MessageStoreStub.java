package ru.mail.track.message;

import ru.mail.track.data.DataService;
import ru.mail.track.message.messagetypes.Message;
import ru.mail.track.message.messagetypes.ChatSendMessage;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 */
public class MessageStoreStub implements MessageStore {

    DataService dataService;

    @Override
    public List<Long> getUsersByChatId(Long chatId) {
        List<Long> users = dataService.getUsersByChatId(chatId);
        return users;
    }

    @Override
    public List<Long> getChatsByUserId(Long userId) {

        List<Long> chats = dataService.getChatsByUserId(userId);
        return chats;

    }

    @Override
    public Chat getChatById(Long chatId) {

        Chat chat = new Chat();
        List<Long> userIds = getUsersByChatId(chatId);
        List<Long> messagesIds = getMessagesFromChat(chatId);

        for(Long userId : userIds) {
            chat.addParticipant(userId);
        }

        for(Long messageId : messagesIds) {
            chat.addMessage(messageId);
        }

        chat.setId(chatId);

        return chat;

    }

    @Override
    public List<Long> getMessagesFromChat(Long chatId) {
        List<Long> messageList = dataService.getMessagesByChatId(chatId);
        return messageList;
    }

    @Override
    public Message getMessageById(Long messageId) {

        return dataService.getMessageById(messageId);

    }

    @Override
    public void addMessage(Long chatId, Message message) {
        dataService.addMessage(chatId, message);
    }

    @Override
    public void addUserToChat(Long userId, Long chatId) {
        dataService.addUserToChat(userId, chatId);
    }

    @Override
    public Chat createChat(Long chatCreator, List<Long> userIds) {
        Chat chat = new Chat();
        chat.setCreatorId(chatCreator);

        dataService.addChat(chat);

        for (Long id : userIds) {
            chat.addParticipant(id);
            addUserToChat(id, chat.getId());
        }

        return chat;
    }

    public MessageStoreStub(DataService dataService) {
        this.dataService = dataService;
    }

}