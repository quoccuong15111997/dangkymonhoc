package com.example.model;

public class Message {
    private String to;
    Notification NotificationObject;


    // Getter Methods

    public String getTo() {
        return to;
    }

    public Notification getNotification() {
        return NotificationObject;
    }

    // Setter Methods

    public void setTo(String to) {
        this.to = to;
    }

    public void setNotification(Notification notificationObject) {
        this.NotificationObject = notificationObject;
    }
}
class Notification {
    private String body;


    // Getter Methods

    public String getBody() {
        return body;
    }

    // Setter Methods

    public void setBody(String body) {
        this.body = body;
    }

    public Notification(String body) {
        this.body = body;
    }
}