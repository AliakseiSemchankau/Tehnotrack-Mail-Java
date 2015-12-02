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
        String text =
                "\n\\login [username, password]\n" +
                "\\chat_create [id1, id2, ...]\n" +
                "\\chat_send [chat_id, text]\n" +
                "\\simple [text]\n" +
                "\\register [username, password]\n" +
                "\\help []\n" +
                "\\user_info [id (default=current)]\n" +
                "\\user_pass [old password, new password]\n" +
                "\\chat_list []\n" +
                "\\chat_history [chat_id, number of comments (default=all comments)]\n" +
                "\\chat_find [chat_id, pattern]\n";
        Result result = new Result(true, "");
        result.setTextMSG(text);
        return result;
    }

}
