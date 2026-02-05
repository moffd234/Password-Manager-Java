package com.moffd.app.Models;

public class UserSession {
    private User user;
    private Byte[] encryptionKey;

    public UserSession(User user, Byte[] encryptionKey) {
        this.user = user;
        this.encryptionKey = encryptionKey;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Byte[] getEncryptionKey() {
        return encryptionKey;
    }

    public void setEncryptionKey(Byte[] encryptionKey) {
        this.encryptionKey = encryptionKey;
    }
}
