package ru.mail.track.perform;

import ru.mail.track.message.MessageStore;
import ru.mail.track.message.MessageWorker;
import ru.mail.track.message.Result;
import ru.mail.track.message.User;
import ru.mail.track.message.UserStorage;
import ru.mail.track.session.Session;

/**
 * Created by aliakseisemchankau on 15.10.15.
 */
public class CommandHandlerHelp implements CommandHandler {
    @Override
    public Result perform(String[] cmd, Session session, UserStorage userStorage, MessageStore messageStore) {
        System.out.println("\\help\n\\user\n\\history\n\\find\n\\exit");
        return new Result();
    }
}
