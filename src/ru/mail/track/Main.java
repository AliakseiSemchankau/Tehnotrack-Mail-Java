package ru.mail.track;

import java.io.IOException;

/**
 * Created by aliakseisemchankau on 29.9.15.
 */
public class Main {

    public static void main(String... argc) throws Exception {
        UserStorage store = new UserStorage("logins.txt", "passwords.txt");
        AuthorizationService service = new AuthorizationService(store);
        service.start();
        store.close("logins.txt", "passwords.txt");
    }

}
