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

    public String readPassword(final String message) {
        String result = null;
        while(result == null || result.length() == 0) {
            System.out.println(message);
            Console console = System.console();
            result = new String(console.readPassword());
        }
        return result;
    }

    public String readString(final String message) {
        String result = null;
        while (result == null || result.length() == 0) {
            System.out.println(message);
            Scanner scan = new Scanner(System.in);
            result = scan.nextLine();
        }

        return result;
    }

    public void start() throws Exception{

        String wish = readString("press 'a' if you'd like to authorize and 'r' for registering");
        if (wish.equals("a")) {
            authorize();
        } else if (wish.equals("r")) {
            createUser();
        } else {
            System.out.println("there are no such option: " + wish);
        }

    }

    public void authorize() throws Exception {
        String userName = readString("enter your username");
        String password = readPassword("enter your password");

        if (store.isUserExist(userName)) {
            user = store.getUser(userName, password);
            if (user != null) {
                logged = true;
                System.out.println("Done! User " + userName + " is authorized");
            } else {
                System.out.println("Password for User " + userName + " is incorrect:(");
            }
        } else {
            System.out.println("This user is unregistered");
        }
    }

    public void createUser() throws Exception {

        while(true) {
            String userName = readString("enter your new username");
            String password = readPassword("enter your password");
            if (store.isUserExist(userName)) {
                System.out.println("such user already exists: " + userName);
                continue;
            }
            System.out.println("registered succesfully: " + userName);
            user = new User(userName, password);
            logged = true;
            store.addUser(user);
            break;
        }
    }
}
