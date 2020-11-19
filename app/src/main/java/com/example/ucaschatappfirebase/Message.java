package com.example.ucaschatappfirebase;

public class Message {

    private String userEmail;
    private String messageBody;
    private long date;

    public Message() {
        //must have empty constructor
    }

    public Message(String userEmail, String messageBody, long date) {
        this.userEmail = userEmail;
        this.messageBody = messageBody;
        this.date = date;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public long getDate() {
        return date;
    }
}
