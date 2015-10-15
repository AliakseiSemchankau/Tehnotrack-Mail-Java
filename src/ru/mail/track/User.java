package ru.mail.track;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by aliakseisemchankau on 29.9.15.
 */
public class User {

    private String userName;
    private byte[] hash;
    private String nickName;

    public User(final String userName, final String password) throws Exception{
        this.userName = userName;
        hash = AuthorizationService.calcHash(password);
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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(final String newNickName) {
        nickName = newNickName;
    }

}
