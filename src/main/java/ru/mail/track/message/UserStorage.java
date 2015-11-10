package ru.mail.track.message;

import ru.mail.track.data.DataService;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by aliakseisemchankau on 29.9.15.
 */
public class UserStorage implements IUserStore {

    private Map<Long, User> users;           // login -> corresponding User
    private DataService dService;          //downloading service for saving user information
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
        if (AuthorizationService.isCorrect(users.get(id), pass)) {
            return users.get(id);
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
    public void initialize(DataService dService) throws Exception {

        this.dService = dService;
        users = dService.downloadUsers();
        for (Map.Entry<Long, User> entry : users.entrySet()) {
            //System.out.println(entry.getKey() + " " + entry.getValue().getName());
        }
        userCounter = new AtomicLong(users.size());
        //System.out.println("////////////");
        for(Long id : users.keySet()){
            //System.out.println(users.get(id).getName() + " " + id.toString());
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
