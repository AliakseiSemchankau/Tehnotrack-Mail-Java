package ru.mail.track.data;

import ru.mail.track.message.Chat;
import ru.mail.track.message.User;
import ru.mail.track.message.messagetypes.Message;

import java.util.List;
import java.util.Map;

/**
 * Created by aliakseisemchankau on 22.10.15.
 */
public interface DataService {

    void init() throws Exception;

   // public Map<Long, User> downloadUsers() throws Exception;

   // public List<Message> readCommentsHistoryUser(String userName);

    User addUser(String userName, String password) throws Exception;

    User getUser(String userName) throws Exception;

    User getUserById(Long id) throws Exception;

    //public void addUserName(String userName) throws Exception;

   // public void addPassword(byte[] password) throws Exception;

   // public void appendCommentsForUser(List<Message> comments, final String userName);

    void setNewPass(String login, String password) throws Exception;

    void addChat(Chat chat);

    void addUserToChat(Long userId, Long chatId);

    List<Long> getChatsByUserId(Long userId);

    List<Long> getUsersByChatId(Long chatId);

    void addMessage(Long chatId, Message msg);

    List<Long> getMessagesByChatId(Long chatId);

    Message getMessageById(Long messageId);

}
