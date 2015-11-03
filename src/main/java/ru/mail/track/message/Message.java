package ru.mail.track.message;

import java.util.Date;

/**
 * Created by aliakseisemchankau on 15.10.15.
 */
public class Message {

    private String message;
    private String timeStamp;
    private Long id;
    private Long sender;

    public Message (final String msg) {
        message = msg;
        timeStamp = new Date().toString();
    }

    public Message (final String msg, final String time) {
        message = msg;
        timeStamp = time;
    }

    public String getMessage() {
        return message;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    @Override
    public String toString() {
        return ("message: " + message + "; timestamp: " + timeStamp + "; " +
                "id: " + id + "; sender: " + sender );
    }

    public Long getId() {
        return id;
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setSender(Long sender) {
        this.sender = sender;
    }
}
