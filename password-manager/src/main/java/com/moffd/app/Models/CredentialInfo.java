package com.moffd.app.Models;

public record CredentialInfo(String site, String username, String password) {

    @Override
    public String site() {
        return site;
    }

    @Override
    public String username() {
        return username;
    }

    @Override
    public String password() {
        return password;
    }
}
