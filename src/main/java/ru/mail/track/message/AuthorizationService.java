package ru.mail.track.message;

import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
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

    public static byte[] calcHash(final String str) {

        try {
            if (md == null) {
                md = MessageDigest.getInstance("SHA-256");
            }
            md.update(str.getBytes("UTF-8"));
        } catch (Exception exc) {
            System.err.println("can't get instance of hashing algo SHA-256");
        }
        return md.digest();
    }

    public static boolean isCorrect(User user, String word) {

        if (word == null) {
            return false;
        }

        String hash = user.getHash();
        String newHash = (new BigInteger(AuthorizationService.calcHash(word))).toString();

        return hash.equals(newHash);
    }

    public AuthorizationService(UserStorage store, InputStream is, OutputStream os) {
        this.store = store;
    }

  /*
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
*/
}
