package ru.mail.track.message.messagetypes;

import ru.mail.track.perform.CommandType;

/**
 * Created by aliakseisemchankau on 3.11.15.
 */
public class LoginMessage extends Message {

    private String login;
    private String password;

    public LoginMessage() {
        setType(CommandType.USER_LOGIN);
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {

        return password;
    }

    public String getLogin() {
        return login;
    }
}
