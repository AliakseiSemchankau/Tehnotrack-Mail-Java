package ru.mail.track;

import ru.mail.track.messageservice.MessageService;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by aliakseisemchankau on 29.9.15.
 */
public class AuthorizationService {

    private UserStorage store;
    private static MessageDigest md;

    private boolean success = false;

    User user = null;

    public static byte[] calcHash(final String str) throws Exception {

        if (md == null) {
            md = MessageDigest.getInstance("SHA-256");
        }
        md.update(str.getBytes("UTF-8"));

        return md.digest();
    }

    public static boolean isCorrect(User user, String word) throws Exception {

        if (word == null) {
            return false;
        }

        byte[] hash = user.getHash();
        byte[] newHash = AuthorizationService.calcHash(word);

        return Arrays.equals(hash, newHash);
    }

    public AuthorizationService(UserStorage store) {
        this.store = store;
    }

    private String readPassword(final String message) {
        String result = null;
        while (result == null || result.length() == 0) {
            System.out.println(message);
            //Console console = System.console();
            //result = new String(console.readPassword());
            Scanner scan = new Scanner(System.in);
            result = scan.nextLine();
        }
        return result;
    }

    private String readString(final String message) {
        String result = null;
        while (result == null || result.length() == 0) {
            System.out.println(message);
            Scanner scan = new Scanner(System.in);
            result = scan.nextLine();
        }

        return result;
    }

    public void start() throws Exception {

        String wish = readString("press 'a' for authorizing, 'r' for registering or 'q' for quit");

        boolean wishIsDone = false;

        if (wish.equals("a")) {
            user = authorize();
            success = (user != null);
            return;
        }

        if (wish.equals("r")) {
            user = createUser();
            success = (user != null);
            return;
        }

        if (wish.equals("q")) {
            return;
        }

        if (!wishIsDone) {
            System.out.println("there is no such option: " + wish);
        }

    }

    public User authorize() throws Exception {
        String userName = readString("enter your username");
        String password = readPassword("enter your password");

        if (store.isUserExist(userName)) {
            User user = store.getUser(userName, password);
            if (user != null) {
                System.out.println("Done! User " + userName + " is authorized");
                return user;
            } else {
                System.out.println("Password for User " + userName + " is incorrect:(");
                return null;
            }
        } else {
            System.out.println("This user is unregistered");
            System.out.println();
            String wish = readString("Print 'r' if you'd like to register or 'q' if you want to quit");

            if (wish.equals("r")) {
                return createUser();
            }

            if (wish.equals("q")) {
                return null;
            }

            System.out.println("there is no such option: " + wish);
            return null;
        }

    }

    public User createUser() throws Exception {

        while (true) {
            String userName = readString("enter your new username");
            String password = readPassword("enter your password");
            if (store.isUserExist(userName)) {
                System.out.println("such user already exists: " + userName);
                continue;
            }
            System.out.println("registered succesfully: " + userName);
            User user = new User(userName, password);
            store.addUser(user);
            return user;
        }
    }

    boolean getSuccess() {
        return success;
    }

    public User getUser() {
        return user;
    }

}
