package com.moffd.app.Models;

public class User {
    private int id;
    private String username;
    private String masterPassword;
    private String email;

    public User(int id, String username, String masterPassword, String email) {
        this.id = id;
        this.username = username;
        this.masterPassword = masterPassword;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMasterPassword() {
        return masterPassword;
    }

    public void setMasterPassword(String masterPassword) {
        this.masterPassword = masterPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
