package com.example.model;

import java.io.Serializable;

public class ThongBao implements Serializable {
    private String user;
    private String title;
    private String messege;
    private String date;

    public ThongBao(String user, String title, String messege, String date) {
        this.user = user;
        this.title = title;
        this.messege = messege;
        this.date = date;
    }

    public ThongBao() {
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessege() {
        return messege;
    }

    public void setMessege(String messege) {
        this.messege = messege;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
