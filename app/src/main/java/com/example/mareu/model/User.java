package com.example.mareu.model;

public class User {

    private String userName, mail;

    public User(String userName, String mail) {
        this.userName = userName;
        this.mail = mail;
    }

    public User(String mail) {
        this.mail = mail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
