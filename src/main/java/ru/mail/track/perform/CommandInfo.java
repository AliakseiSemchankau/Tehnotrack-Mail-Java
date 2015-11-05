package ru.mail.track.perform;

import ru.mail.track.message.MessageStore;
import ru.mail.track.message.Result;
import ru.mail.track.message.User;
import ru.mail.track.message.UserStorage;
import ru.mail.track.message.messagetypes.InfoMessage;
import ru.mail.track.message.messagetypes.Message;
import ru.mail.track.session.Session;

/**
 * Created by aliakseisemchankau on 5.11.15.
 */
public class CommandInfo implements Command {
    @Override
    public Result perform(Message msg, Session session, UserStorage userStorage, MessageStore messageStore) {
        InfoMessage infoMessage = (InfoMessage) msg;
        if (!infoMessage.isHasArg()) {
            if (session.getSessionUser() == null) {
                return new Result(false, "session " + session.getId() + " has no registered user");
            }
            infoMessage.setUserId(session.getSessionUser().getUserID());
        }

        User user = userStorage.getUserById(infoMessage.getUserId());
        Result result = new Result(true, "");
        result.setTextMSG("Info about user with id=" + user.getUserID() + ", login=" + user.getName());
        return result;
    }
}
