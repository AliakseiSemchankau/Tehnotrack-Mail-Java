package ru.mail.track.message.messagetypes;

import ru.mail.track.perform.CommandType;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by aliakseisemchankau on 15.10.15.
 */
public class Message implements Serializable {

    private String message;
    private String timeStamp;
    private Long messageId;
    private Long sender;
    private CommandType type;

    public Message(){
        timeStamp = new Date().toString();
    }

    public String getMessage() {
        return message;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    @Override
    public String toString() {
        return "Message{" +
                ", message=" + message +
                ", id=" + messageId +
                ", sender=" + sender +
                ", type=" + type +
                '}';
    }

    public Long getMessageId() {
        return messageId;
    }

    public Long getSender() {
        return sender;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public void setSender(Long sender) {
        this.sender = sender;
    }

    public CommandType getType() {
        return type;
    }

    public void setType(CommandType type) {
        this.type = type;
    }
}
