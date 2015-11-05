package ru.mail.track.message;

import ru.mail.track.download.DownloadService;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by aliakseisemchankau on 29.9.15.
 */
public class UserStorage implements IUserStore {

    private Map<Long, User> users;           // login -> corresponding User
    private DownloadService dService;          //downloading service for saving user information
    private Map<String, Long> userLogins = new HashMap<>();
    private AtomicLong userCounter;

    // Добавить пользователя в хранилище
    @Override
    public User addUser(User user) {

        try {
            Lock lock = new ReentrantLock();
            lock.lock();
            try {

               // dService.addUserName(user.getName());
                //dService.addPassword(user.getHash());
                userCounter.incrementAndGet();
                dService.addUser(user.getName(), user.getHash(), userCounter.longValue());
                userLogins.put(user.getName(), userCounter.longValue());
                users.put(userCounter.longValue(), user);
                user.setUserID(userCounter.longValue());
            } finally {
                lock.unlock();
            }
        } catch (Exception ioExc) {
            System.err.println(ioExc.getMessage());
            return null;
        }
        return user;
    }

    // Получить пользователя по имени и паролю
    @Override
    public User getUser(String name, String pass) {
        Long id = userLogins.get(name);
        if (id == null) {
            return null;
        }
        if (AuthorizationService.isCorrect(users.get(name), pass)) {
            return users.get(name);
        }
        return null;
    }

    @Override
    public User getUserById(Long id) {
        if (!users.containsKey(id)) {
            return null;
        }
        return users.get(id);
    }

    @Override
    public void initialize(DownloadService dService) throws Exception {

        users = dService.downloadUsers();
        for (Map.Entry<Long, User> entry : users.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue().getName());
        }
        userCounter = new AtomicLong(users.size());
        this.dService = dService;
        for(Long id : users.keySet()){
            userLogins.put(users.get(id).getName(), id);
        }
    }

    @Override
    public boolean isUserExist(String login) {
        return userLogins.containsKey(login);
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
