package ru.mail.track.message.messagetypes;

import ru.mail.track.perform.CommandType;

/**
 * Created by aliakseisemchankau on 5.11.15.
 */
public class PassMessage extends Message {

    private String oldPass;
    private String newPass;

    public PassMessage() {
        setType(CommandType.USER_PASS);
    }

    public String getNewPass() {
        return newPass;
    }

    public void setNewPass(String newPass) {
        this.newPass = newPass;
    }

    public String getOldPass() {

        return oldPass;
    }

    public void setOldPass(String oldPass) {
        this.oldPass = oldPass;
    }
}
