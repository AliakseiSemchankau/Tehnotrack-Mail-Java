package ru.mail.track.messageservice.perform;

import ru.mail.track.messageservice.Message;
import ru.mail.track.messageservice.MessageService;

import java.util.List;

/**
 * Created by aliakseisemchankau on 15.10.15.
 */
public class MessageServiceCommandFind extends MessageServiceCommand {
    @Override
    public void perform(String[] cmd, MessageService ms) {

        if (cmd.length < 2) {
            System.out.println("no pattern to find");
            return;
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

    }
}
