package ru.mail.track.message;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * Created by aliakseisemchankau on 29.9.15.
 */
public class User {

    private String userName;
    //private byte[] hash;
    private String hash;
    private Long userID;

    public User(final String userName) {
        this.userName = userName;
    }

    public void setHash(final String hash) {
        this.hash = hash;
    }

    public User(final String userName, final String password) {
        this.userName = userName;
        hash = (new BigInteger(AuthorizationService.calcHash(password))).toString();
    }

    /*
    public User(final String userName, final byte[] hash) {
        this.userName = userName;
        this.hash = (new BigInteger(hash)).toString();
    }
*/

    public String getName() {
        return userName;
    }

    /*
    public byte[] getHash() {
        return hash;
    }
    */

    public String getHash() {
        return hash;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long id) {
        userID = id;
    }

    public void setPass(final String password) {
        hash = (new BigInteger(AuthorizationService.calcHash(password))).toString();
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", hash=" + hash +
                ", userID=" + userID +
                '}';
    }
}
