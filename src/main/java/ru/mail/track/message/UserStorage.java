package ru.mail.track.message;

import ru.mail.track.download.DownloadService;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by aliakseisemchankau on 29.9.15.
 */
public class UserStorage implements IUserStore {

    private Map<Long, User> users;           // login -> corresponding User
    private DownloadService dService;          //downloading service for saving user information

    // Добавить пользователя в хранилище
    @Override
    public User addUser(User user) {
        users.put(user.getUserID(), user);
        try {
            dService.addUserName(user.getName());
            dService.addPassword(user.getHash());
        } catch (Exception ioExc) {
            System.err.println(ioExc.getMessage());
            return null;
        }
        return user;
    }

    // Получить пользователя по имени и паролю
    @Override
    public User getUser(String name, String pass) {
        if (users.containsKey(name) && AuthorizationService.isCorrect(users.get(name), pass)) {
            return users.get(name);
        }
        return null;
    }

    @Override
    public User getUserById(Long id) {
        return null;
    }

    @Override
    public void initialize(DownloadService dService) throws Exception {
        users = dService.downloadUsers();
        this.dService = dService;
    }

    /*
    public void saveUserChanges(String userName) {

        List<Message> comments = commentHistory.get(userName);

        if (comments.size() == commentCount.get(userName).intValue()) {  // if user didn't add any comment, we don't change anything
            return;
        }

        List<Message> newComments = comments.subList(commentCount.get(userName).intValue(), comments.size()); //comments to update

        dService.appendCommentsForUser(newComments, userName);

    }
*/
}
