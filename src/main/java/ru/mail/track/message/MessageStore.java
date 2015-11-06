package ru.mail.track.message;

import ru.mail.track.message.messagetypes.Message;

import java.util.List;

/**
 * Хранилище информации о сообщениях
 */
public interface MessageStore {

    /**
     *
     Получить список id пользователей данного чата
     */
    List<Long> getUsersByChatId(Long chatId);

    /**
     получаем список чатов данного юзера
     */
    List<Long> getChatsByUserId(Long userId);

    /**
     получить информацию о чате
     */
    Chat getChatById(Long chatId);

    /**
     * Список сообщений из чата
     *
     */
    List<Long> getMessagesFromChat(Long chatId);

    /**
     * Получить информацию о сообщении
     */
    Message getMessageById(Long messageId);

    /**
     * Добавить сообщение в чат
     */
    void addMessage(Long chatId, Message message);

    /**
     * Добавить пользователя к чату
     */
    void addUserToChat(Long userId, Long chatId);

    Chat createChat(List<Long> userIds);

}