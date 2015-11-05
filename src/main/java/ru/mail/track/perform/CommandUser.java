package ru.mail.track.perform;

import ru.mail.track.message.messagetypes.Message;
import ru.mail.track.message.MessageStore;
import ru.mail.track.message.Result;
import ru.mail.track.message.UserStorage;
import ru.mail.track.session.Session;

/**
 * Created by aliakseisemchankau on 15.10.15.
 */
public class CommandUser implements Command {
    @Override
    public Result perform(Message msg, Session session, UserStorage userStorage, MessageStore messageStore) {
        /*if (cmd.length < 2) {
            System.out.println("you forgot to add the new alias");
            return;
        }
        String newNickName = cmd[1];
        System.out.println("your old nickname is " + ms.getNickName());
        ms.setNickName(newNickName);
        System.out.println("your new nickname is " + ms.getNickName());
    }*/
        return null;
    }
}
