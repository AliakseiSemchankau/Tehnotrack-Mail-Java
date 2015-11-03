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
public interface CommandHandler {

    Result perform(String[] cmd, Session session, UserStorage userStorage, MessageStore messageStore);

}
