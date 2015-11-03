package ru.mail.track.perform;

import ru.mail.track.message.Message;
import ru.mail.track.message.MessageStore;
import ru.mail.track.message.MessageWorker;
import ru.mail.track.message.Result;
import ru.mail.track.message.UserStorage;
import ru.mail.track.session.Session;

import java.util.List;

/**
 * Created by aliakseisemchankau on 15.10.15.
 */
public class CommandHandlerFind implements CommandHandler {
    @Override
    public Result perform(String[] cmd, Session session, UserStorage userStorage, MessageStore messageStore) {

        if (cmd.length < 2) {
            System.out.println("no pattern to find");
            return new Result;
        }

        String pattern = cmd[1];
        pattern = ".*" + pattern + ".*";

        List<Message> comments = ms.getCommentsHistory();

        for(Message comment: comments) {
            try {
                if (comment.getMessage().matches(pattern)) {
                    System.out.println(comment.getTimeStamp());
                    System.out.println(comment.getMessage());
                }
            } catch (java.util.regex.PatternSyntaxException psExc) {
                System.out.println("sorry, but " + pattern + " can't be parsed normally");
            }
        }

        return new Result();

    }
}
