package com.moffd.app.Models;

import javax.crypto.SecretKey;

public class UserSession {
    private final User user;
    private final SecretKey encryptionKey;

    public UserSession(User user, SecretKey encryptionKey) {
        this.user = user;
        this.encryptionKey = encryptionKey;
    }

    public User getUser() {
        return user;
    }

    public SecretKey getEncryptionKey() {
        return encryptionKey;
    }
}
