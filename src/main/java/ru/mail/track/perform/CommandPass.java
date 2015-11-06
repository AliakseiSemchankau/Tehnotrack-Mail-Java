package ru.mail.track.perform;

import ru.mail.track.message.MessageStore;
import ru.mail.track.message.Result;
import ru.mail.track.message.User;
import ru.mail.track.message.UserStorage;
import ru.mail.track.message.messagetypes.Message;
import ru.mail.track.message.messagetypes.PassMessage;
import ru.mail.track.session.Session;

/**
 * Created by aliakseisemchankau on 5.11.15.
 */
public class CommandPass implements Command {
    @Override
    public Result perform(Message msg, Session session, UserStorage userStorage, MessageStore messageStore) {

        PassMessage passMessage = (PassMessage) msg;

        User user = session.getSessionUser();
        if (user == null) {
            return new Result(false, "you weren't authorized for changing the password");
        }

        if (userStorage.getUser(user.getName(), passMessage.getOldPass()) == null) {
            return new Result(false, "your password for login=" + user.getName() + ", id=" + user.getUserID() + "is wrong");
        }

        user.setPass(passMessage.getNewPass());

        return new Result(true, "", "you've succesfully changed the password");

    }
}
