package ru.mail.track.perform;

import ru.mail.track.message.messagetypes.ChatFindMessage;
import ru.mail.track.message.messagetypes.Message;
import ru.mail.track.message.MessageStore;
import ru.mail.track.message.Result;
import ru.mail.track.message.User;
import ru.mail.track.message.UserStorage;
import ru.mail.track.session.Session;

import java.util.List;

/**
 * Created by aliakseisemchankau on 15.10.15.
 */
public class CommandChatFind implements Command {
    @Override
    public Result perform(Message msg, Session session, UserStorage userStorage, MessageStore messageStore) {

        ChatFindMessage chatFindMessage = (ChatFindMessage) msg;

        String pattern = chatFindMessage.getPattern();
        pattern = ".*" + pattern + ".*";

        User user = session.getSessionUser();

        Long chatId = chatFindMessage.getChatId();

        if (user == null) {
            return new Result(false, "you weren't authorized:(");
        }

        if (!messageStore.getChatById(chatId).getParticipantIds().contains(user.getUserID())) {
            return new Result(false, "user with id " + user.getUserID() + " wasn't invited to chat " + chatId);
        }

        List<Long> messageIds = messageStore.getChatById(chatId).getMessageIds();

        StringBuilder sb = new StringBuilder();

        for (Long id : messageIds) {
            Message chatMsg = messageStore.getMessageById(id);
            try {
                if (chatMsg.getMessage().matches(pattern)) {
                    sb.append(chatMsg.toString());
                    sb.append("\n");
                }
            } catch (java.util.regex.PatternSyntaxException psExc) {
                return new Result(false, "sorry, but pattern " + pattern + "can't be parsed normally");
            }
        }

        Result result = new Result(true, "");
        result.setTextMSG("Satisfiable messages are: \n" + sb.toString());
        return result;

    }
}
