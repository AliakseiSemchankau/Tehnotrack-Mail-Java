package ru.mail.track;

import java.io.IOException;

/**
 * Created by aliakseisemchankau on 29.9.15.
 */
public class Main {

    public static void main(String... argc) throws Exception {

        //User.printHash(User.calcHash("123"));

        //System.out.println(System.getProperty("user.dir"));
        UserStorage store = new UserStorage("logins.txt", "passwords.txt");
        AuthorizationService service = new AuthorizationService(store);
        service.authorize();
        store.close("logins.txt", "passwords.txt");

    }

}
