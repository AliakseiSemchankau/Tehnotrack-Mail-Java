package ru.mail.track.message.messagetypes;

import ru.mail.track.perform.CommandType;

/**
 * Created by aliakseisemchankau on 4.11.15.
 */
public class RegisterMessage extends Message {

    private String login;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String password;

    public RegisterMessage() {
        setType(CommandType.USER_REGISTER);
    }

}
