package ru.mail.track.perform;

import ru.mail.track.message.User;
import ru.mail.track.message.messagetypes.ChatHistoryMessage;
import ru.mail.track.message.messagetypes.Message;
import ru.mail.track.message.MessageStore;
import ru.mail.track.message.Result;
import ru.mail.track.message.UserStorage;
import ru.mail.track.session.Session;

import java.util.List;

/**
 * Created by aliakseisemchankau on 15.10.15.
 */
public class CommandChatHistory implements Command {
    @Override
    public Result perform(Message msg, Session session, UserStorage userStorage, MessageStore messageStore) {

        ChatHistoryMessage chatHistoryMessage = (ChatHistoryMessage) msg;

        Long chatId = chatHistoryMessage.getChatId();

        User user = session.getSessionUser();
        if (user == null) {
            return new Result(false, "you weren't authorized");
        }

        if (!messageStore.getChatById(chatId).getParticipantIds().contains(user.getUserID())) {
            return new Result(false, "user with id=" + user.getUserID() + " wasn't invited to chat with id=" + chatId);
        }

        List<Long> commentsHistory = messageStore.getMessagesFromChat(chatId);

        long countOfComments = commentsHistory.size();

        if (chatHistoryMessage.isHasArg()) {
            countOfComments = Math.min(countOfComments, chatHistoryMessage.getCountOfMessages());
        }

        System.out.println("last " + countOfComments + " are:");

        Result result = new Result(true, "");
        StringBuilder textMsg = new StringBuilder();

        for (long i = commentsHistory.size() - countOfComments; i < commentsHistory.size(); ++i) {
            textMsg.append(messageStore.getMessageById(commentsHistory.get((int)i)).toString() + "\n");
        }

        result.setTextMSG(textMsg.toString());

        return result;
    }



}
