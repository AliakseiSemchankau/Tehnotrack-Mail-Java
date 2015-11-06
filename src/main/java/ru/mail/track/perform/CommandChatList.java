package ru.mail.track.perform;

import ru.mail.track.message.MessageStore;
import ru.mail.track.message.Result;
import ru.mail.track.message.User;
import ru.mail.track.message.UserStorage;
import ru.mail.track.message.messagetypes.ChatListMessage;
import ru.mail.track.message.messagetypes.Message;
import ru.mail.track.session.Session;

import java.util.List;

/**
 * Created by aliakseisemchankau on 6.11.15.
 */
public class CommandChatList implements Command {
    @Override
    public Result perform(Message msg, Session session, UserStorage userStorage, MessageStore messageStore) {

        ChatListMessage chatListMessage = (ChatListMessage) msg;

        User user = session.getSessionUser();
        if (user == null) {
            return new Result(false, "you weren't authorized");
        }

        List<Long> chatIds = messageStore.getChatsByUserId(user.getUserID());
        if (chatIds == null) {
            return new Result(true, "", "you aren't taking part in any chat");
        }

        StringBuilder chatList = new StringBuilder();
        for(Long chatId : chatIds) {
            chatList.append("id="+chatId.toString()+"\n");
        }

        return new Result(true, "", "your chat are:\n" + chatList);

    }
}