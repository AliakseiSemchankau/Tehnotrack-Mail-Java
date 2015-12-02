package ru.mail.track.perform;

import ru.mail.track.message.messagetypes.Message;
import ru.mail.track.message.MessageStore;
import ru.mail.track.message.Result;
import ru.mail.track.message.UserStorage;
import ru.mail.track.session.Session;

/**
 * Created by aliakseisemchankau on 15.10.15.
 */
public interface Command {

    Result perform(Message msg, Session session, UserStorage userStorage, MessageStore messageStore);

}
