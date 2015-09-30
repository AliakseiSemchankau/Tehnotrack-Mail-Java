package ru.mail.track;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by aliakseisemchankau on 29.9.15.
 */
public class User {

    private String userName;
    private byte[] hash;

    /*
    public static void printHash(final byte[] bytes) {
        for(int i= 0; i< 32; ++i) {
            System.out.print(bytes[i] + " ");
        }
        System.out.println();
    }
    */

    public static byte[] calcHash(final String str) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(str.getBytes("UTF-8")); // Change this to "UTF-16" if needed
        return md.digest();
    }

    boolean isCorrect(String word) throws Exception{
        if (word == null) {
            return false;
        }

        byte[] newHash = calcHash(word);

        for(int i = 0; i < 32; ++i) {
            if (hash[i] != newHash[i]) {
                return false;
            }
        }

        return true;
    }

    public User(final String userName, final String password) throws Exception{
        this.userName = userName;
        hash = calcHash(password);
        //System.out.println(hash + " - hash for " + userName + "+" + password);
    }

    public User(final String userName, final byte[] hash) throws Exception {
        this.userName = userName;
        this.hash = hash;
    }

    public String getName() {
        return userName;
    }

    public byte[] getHash() {
        return hash;
    }

}
