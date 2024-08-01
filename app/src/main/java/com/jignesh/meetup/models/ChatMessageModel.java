package com.jignesh.meetup.models;

public class ChatMessageModel {
    public String senderId;
    public String message;
    public String time;
    public String date;

    public ChatMessageModel(String senderId, String message, String time, String date) {
        this.senderId = senderId;
        this.message = message;
        this.time = time;
        this.date = date;
    }
}
