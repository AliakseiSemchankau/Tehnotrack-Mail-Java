package ru.mail.track.messageservice;

import java.util.Date;

/**
 * Created by aliakseisemchankau on 15.10.15.
 */
public class Message {

    private String message;
    private String timeStamp;

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

}
