package ru.mail.track.message.messagetypes;

import ru.mail.track.perform.CommandType;

/**
 * Created by aliakseisemchankau on 5.11.15.
 */
public class InfoMessage extends Message {

    private boolean hasArg = false;
    private Long userId = null;

    public boolean isHasArg() {
        return hasArg;
    }

    public void setHasArg(boolean hasArg) {
        this.hasArg = hasArg;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public InfoMessage() {
        setType(CommandType.USER_INFO);
    }


}
