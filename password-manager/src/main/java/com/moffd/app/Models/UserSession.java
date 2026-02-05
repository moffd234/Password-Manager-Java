package com.moffd.app.Models;

public class UserSession {
    private final User user;
    private final Byte[] encryptionKey;

    public UserSession(User user, Byte[] encryptionKey) {
        this.user = user;
        this.encryptionKey = encryptionKey;
    }

    public User getUser() {
        return user;
    }
    public Byte[] getEncryptionKey() {
        return encryptionKey;
    }
}
