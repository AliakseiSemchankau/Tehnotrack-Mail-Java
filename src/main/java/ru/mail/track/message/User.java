package ru.mail.track.message;

/**
 * Created by aliakseisemchankau on 29.9.15.
 */
public class User {

    private String userName;
    private byte[] hash;

    private Long userID;

    public User(final String userName, final String password) {
        this.userName = userName;
        hash = AuthorizationService.calcHash(password);
    }

    public User(final String userName, final byte[] hash) {
        this.userName = userName;
        this.hash = hash;
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

    public void setUserID(Long id) {
        userID = id;
    }



}
