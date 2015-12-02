package ru.mail.track.perform;

import ru.mail.track.message.MessageStore;
import ru.mail.track.message.Result;
import ru.mail.track.message.User;
import ru.mail.track.message.UserStorage;
import ru.mail.track.message.messagetypes.ChatSendMessage;
import ru.mail.track.message.messagetypes.Message;
import ru.mail.track.net.SessionManager;
import ru.mail.track.session.Session;

import java.util.List;

/**
 * Created by aliakseisemchankau on 6.11.15.
 */
public class CommandChatSend implements Command {
    @Override
    public Result perform(Message msg, Session session, UserStorage userStorage, MessageStore messageStore) {

        ChatSendMessage chatSendMessage = (ChatSendMessage) msg;

        User user = session.getSessionUser();
        if (user == null) {
            return new Result(false, "you weren't authorized");
        }

        if (!messageStore.getChatById(chatSendMessage.getChatId()).getParticipantIds().contains(user.getUserID())) {
            return new Result(false, "user with id=" + user.getUserID() +
                    "wasn't invited to chat with id=" + chatSendMessage.getChatId());
        }

        chatSendMessage.setSender(user.getUserID());

        messageStore.addMessage(chatSendMessage.getChatId(), chatSendMessage);

        List<Long> userIds = messageStore.getChatById(chatSendMessage.getChatId()).getParticipantIds();



        for (Long userId : userIds) {
            //System.out.println("COMMAND CHAT SEND participant id: " + userId.toString());
            try {
                SessionManager sessionManager = session.getSessionManager();
                Session userSession = sessionManager.getSessionByUser(userId);
                if (userSession == null) {
                    continue;
                }
                userSession.send(chatSendMessage);
                //session.getSessionManager().getSession(userId).send(chatSendMessage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return new Result(true, "", "your message has been sended succesfully!");

    }
}
