package ru.mail.track.perform;

import ru.mail.track.message.MessageStore;
import ru.mail.track.message.MessageWorker;
import ru.mail.track.message.Result;
import ru.mail.track.message.User;
import ru.mail.track.message.UserStorage;
import ru.mail.track.session.Session;

/**
 * Created by aliakseisemchankau on 2.11.15.
 */
public class CommandHandlerLogin implements CommandHandler {
    @Override
    public Result perform(String[] cmd, Session session, UserStorage userStorage, MessageStore messageStore) {

    }
}