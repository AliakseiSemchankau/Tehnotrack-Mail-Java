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
    //private AtomicLong userCounter;

    // Добавить пользователя в хранилище
    @Override
    public User addUser(User user) {


        try {
            //userCounter.incrementAndGet();
            dService.addUser(user.getName(), user.getHash());
            //userLogins.put(user.getName(), user.getUserID());
            //users.put(user.getUserID().longValue(), user);
            //user.setUserID(userCounter.longValue());

        } catch (Exception ioExc) {
            System.err.println(ioExc.getMessage());
            return null;
        }
        return user;
    }

    // Получить пользователя по имени и паролю
    @Override
    public User getUser(String name, String pass) {

        User user;

        try {
            user = dService.getUser(name);
        } catch (Exception e) {
            System.out.println("some troubles with downloading user with login=" + name);
            return null;
        }

        if (user == null) {
            return null;
        }

        if (AuthorizationService.isCorrect(user, pass)) {
            return user;
        }
        return null;
    }

    @Override
    public User getUserById(Long id) {

        try {
            return dService.getUserById(id);
        } catch (Exception e) {
            System.err.println("UserStorage:getUserById failed to get user with id=" + id);
        }
        return null;
    }

    @Override
    public void initialize(DataService dService) throws Exception {

        this.dService = dService;
        //users = dService.downloadUsers();

        //userCounter = new AtomicLong(users.size());

        //for (Long id : users.keySet()) {
        //    userLogins.put(users.get(id).getName(), id);
       // }
    }

    @Override
    public boolean isUserExist(String login) {
        try {
            return (dService.getUser(login) != null);
        } catch (Exception e) {
            System.err.println("can't check existing of user with login="+login);
            e.printStackTrace();

        }
        return false;
    }

    @Override
    public void updateUserPass(User user) {

        try {
            dService.setNewPass(user.getName(), user.getHash());
        } catch (Exception e) {
            System.err.println("UserStorage:updateUserPass failed to update pass for user="+user.getName());
            e.printStackTrace();
        }
    }


}
