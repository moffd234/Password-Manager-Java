package com.moffd.app.Models;

public class Credential {
    private int id;
    private int userId;
    private String site;
    private String siteUsername;
    private String sitePassword;
    private byte[] iv;

    public Credential(int id, int userId, String site, String siteUsername, String sitePassword) {
        this.id = id;
        this.userId = userId;
        this.site = site;
        this.siteUsername = siteUsername;
        this.sitePassword = sitePassword;
    }

    public Credential(int id, int userId, String site, String siteUsername, String sitePassword, byte[] iv) {
        this.id = id;
        this.userId = userId;
        this.site = site;
        this.siteUsername = siteUsername;
        this.sitePassword = sitePassword;
        this.iv = iv;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getSiteUsername() {
        return siteUsername;
    }

    public void setSiteUsername(String siteUsername) {
        this.siteUsername = siteUsername;
    }

    public String getSitePassword() {
        return sitePassword;
    }

    public void setSitePassword(String sitePassword) {
        this.sitePassword = sitePassword;
    }

    public byte[] getIv() {
        return iv;
    }

    public void setIv(byte[] iv) {
        this.iv = iv;
    }
}