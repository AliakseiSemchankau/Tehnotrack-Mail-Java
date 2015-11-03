package ru.mail.track.message;

/**
 * Created by aliakseisemchankau on 29.9.15.
 */
public class User {

    private String userName;
    private byte[] hash;

    private Long userID;

    public User(final String userName, final String password, final Long userID) throws Exception{
        this.userName = userName;
        hash = AuthorizationService.calcHash(password);
        this.userID = userID;
    }

    public User(final String userName, final byte[] hash, final Long userID) throws Exception {
        this.userName = userName;
        this.hash = hash;
        this.userID = userID;
    }

    public String getName() {
        return userName;
    }

    public byte[] getHash() {
        return hash;
    }

    public Long getUserID() {
        return userID;
    }



}
