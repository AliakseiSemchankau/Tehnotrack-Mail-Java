package ru.mail.track.message.messagetypes;

import ru.mail.track.perform.CommandType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aliakseisemchankau on 6.11.15.
 */
public class ChatCreateMessage extends Message {

    private List<Long> chatUserIds;

    public ChatCreateMessage() {
        setType(CommandType.CHAT_CREATE);
        chatUserIds = new ArrayList<>();
    }

    public List<Long> getChatUserIds() {
        return chatUserIds;
    }

    public void setChatUserIds(List<Long> chatUserIds) {
        this.chatUserIds = chatUserIds;
    }

    public void addId(Long id) {
        if (!chatUserIds.contains(id)) {
            chatUserIds.add(id);
        }
    }



}
