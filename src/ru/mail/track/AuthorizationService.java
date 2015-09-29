package ru.mail.track;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by aliakseisemchankau on 29.9.15.
 */
public class AuthorizationService {

    private UserStorage store;
    boolean logged = false;
    User user = null;

    public AuthorizationService(UserStorage store) {
        this.store = store;
    }

    public String readString(final String message) {
        String result = null;
        while (result == null) {
            System.out.println(message);
            Scanner scan = new Scanner(System.in);
            result = scan.nextLine();
        }

        return result;
    }

    public void authorize() throws Exception {
        String userName = readString("enter your username");
        String password = readString("enter your password");

        if (store.isUserExist(userName)) {
            user = store.getUser(userName, password);
            if (user != null) {
                logged = true;
                System.out.println("Done! User " + userName + " is authorized");
            } else {
                System.out.println("Password for User " + userName + " is incorrect:(");
            }
        } else {
            System.out.println("Register please");
            createUser();
        }
    }

    public void createUser() throws Exception {
        String userName = readString("enter your new username");
        String password = readString("enter your password");
        user = new User(userName, password);
        logged = true;
        store.addUser(user);
    }
}
