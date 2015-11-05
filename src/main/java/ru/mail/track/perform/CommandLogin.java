package ru.mail.track.perform;

import ru.mail.track.message.messagetypes.LoginMessage;
import ru.mail.track.message.messagetypes.Message;
import ru.mail.track.message.MessageStore;
import ru.mail.track.message.Result;
import ru.mail.track.message.User;
import ru.mail.track.message.UserStorage;
import ru.mail.track.session.Session;

/**
 * Created by aliakseisemchankau on 2.11.15.
 */
public class CommandLogin implements Command {

    @Override
    public Result perform(Message msg, Session session, UserStorage userStorage, MessageStore messageStore) {

        LoginMessage loginMsg = (LoginMessage) msg;

        String userName = loginMsg.getLogin();
        String password = loginMsg.getPassword();

        if (!userStorage.isUserExist(userName)) {
            return new Result(false, "such user doesn't exist");
        }

        User user = userStorage.getUser(userName, password);

        if (user == null) {
            return new Result(false, "login or password is incorrect");
        }

        session.setSessionUser(user);

        return new Result(true, "");

    }
}