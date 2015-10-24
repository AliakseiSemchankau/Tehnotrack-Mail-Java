package ru.mail.track.messageservice.perform;

import ru.mail.track.messageservice.Message;
import ru.mail.track.messageservice.MessageService;

import java.util.List;

/**
 * Created by aliakseisemchankau on 15.10.15.
 */
public class MessageServiceCommandHistory extends MessageServiceCommand {
    @Override
    public void perform(String[] cmd, MessageService ms) {

        List<Message> commentsHistory = ms.getCommentsHistory();

        int countOfComments = commentsHistory.size();

        if (cmd.length > 1) {
            countOfComments = parseToInt(cmd[1]);
        }

        System.out.println("last " + countOfComments + " are:");

        for(int i = Math.max(commentsHistory.size() - countOfComments, 0); i < commentsHistory.size(); ++i) {
            System.out.println(commentsHistory.get(i).getTimeStamp() + "\n" + commentsHistory.get(i).getMessage());
        }

    }

    private int parseToInt(String number) {

        if (number == null || number.length() == 0) {
            return 0;
        }

        if (!number.matches("[0-9]*")) {
            System.out.println(number + " is not a number");
            return 0;
        }

        int result = 0;
        while (number.length() > 0) {
            String subNumber = number.substring(0, number.length() - 1);
            return (10 * parseToInt(subNumber) + (number.charAt(number.length() - 1) - '0'));
        }

        return result;

    }

}
