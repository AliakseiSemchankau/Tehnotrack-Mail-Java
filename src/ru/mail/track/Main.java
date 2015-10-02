package ru.mail.track;

import java.io.IOException;

/**
 * Created by aliakseisemchankau on 29.9.15.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        UserStorage store = new UserStorage("logins.txt", "passwords.txt");
        store.open();
        AuthorizationService service = new AuthorizationService(store);
        service.start();
    }

}
