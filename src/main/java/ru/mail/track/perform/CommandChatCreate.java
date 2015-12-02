package ru.mail.track.perform;

import ru.mail.track.message.Chat;
import ru.mail.track.message.MessageStore;
import ru.mail.track.message.Result;
import ru.mail.track.message.User;
import ru.mail.track.message.UserStorage;
import ru.mail.track.message.messagetypes.ChatCreateMessage;
import ru.mail.track.message.messagetypes.Message;
import ru.mail.track.session.Session;

import java.util.List;

/**
 * Created by aliakseisemchankau on 6.11.15.
 */
public class CommandChatCreate implements Command {
    @Override
    public Result perform(Message msg, Session session, UserStorage userStorage, MessageStore messageStore) {
        ChatCreateMessage chatCreateMessage = (ChatCreateMessage) msg;

        User user = session.getSessionUser();

        if (user == null) {
            return new Result(false, "you weren't authorized to create a chat");
        }

        chatCreateMessage.addId(user.getUserID());

        if (chatCreateMessage.getChatUserIds().size() < 2) {
            return new Result(false, "you need at least two persons to create a chat");
        }

        List<Long> chatIds = chatCreateMessage.getChatUserIds();

        for(Long id : chatIds) {
            if (userStorage.getUserById(id) == null) {
                return new Result(false, "user with id=" + id.toString() + "does not exist");
            }
        }

        if (chatIds.size() == 2) {
            List<Long> userChatIds = messageStore.getChatsByUserId(user.getUserID());
            for(Long chatId : userChatIds) {
                Chat currentChat = messageStore.getChatById(chatId);
                List<Long> participants = currentChat.getParticipantIds();
                if (participants.size() == 2 && participants.containsAll(chatCreateMessage.getChatUserIds())) {
                    return new Result(true, "", "chat with user ids " + chatCreateMessage.getChatUserIds() +
                    " already exists, id=" + currentChat.getId());
                }
            }
        }

        Chat chat = messageStore.createChat(user.getUserID(), chatIds);


        StringBuilder usersInChat = new StringBuilder();
        for(Long id : chatIds) {
            usersInChat.append("id=" + id.toString() + " ");
        }

        return new Result(true, "", "chat with chatId=" + chat.getId() +
                "is created succesfully. User id's are:" + usersInChat.toString());

    }
}
