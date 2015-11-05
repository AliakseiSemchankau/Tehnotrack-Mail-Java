package ru.mail.track.perform;

import ru.mail.track.message.messagetypes.Message;
import ru.mail.track.message.MessageStore;
import ru.mail.track.message.messagetypes.RegisterMessage;
import ru.mail.track.message.Result;
import ru.mail.track.message.User;
import ru.mail.track.message.UserStorage;
import ru.mail.track.session.Session;

/**
 * Created by aliakseisemchankau on 4.11.15.
 */
public class CommandRegister implements Command {
    @Override
    public Result perform(Message msg, Session session, UserStorage userStorage, MessageStore messageStore) {

        RegisterMessage registerMessage = (RegisterMessage) msg;
        String login = registerMessage.getLogin();
        String password = registerMessage.getPassword();

        if (userStorage.isUserExist(login)) {
            return new Result(false, "user with login=" + login + " already exists");
        }

        User user = new User(login, password);

        userStorage.addUser(user);
        session.setSessionUser(user);

        return new Result(true, "");

    }
}
