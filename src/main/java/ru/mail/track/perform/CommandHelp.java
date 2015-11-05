package ru.mail.track.perform;

import ru.mail.track.message.messagetypes.Message;
import ru.mail.track.message.MessageStore;
import ru.mail.track.message.Result;
import ru.mail.track.message.UserStorage;
import ru.mail.track.session.Session;

/**
 * Created by aliakseisemchankau on 15.10.15.
 */
public class CommandHelp implements Command {

    @Override
    public Result perform(Message msg, Session session, UserStorage userStorage, MessageStore messageStore) {
        String text = "\\help\n\\history\n\\find\n";
        Result result = new Result(true, "");
        result.setTextMSG(text);
        return result;
    }

}
