package com.jignesh.meetup.models;

public class ChatUserModel {
    public String chatId;
    public int profileImg;
    public String name;
    public String lastMsg;
    public String time;

    public ChatUserModel(String chatId, int profileImg, String name, String lastMsg, String time) {
        this.chatId = chatId;
        this.profileImg = profileImg;
        this.name = name;
        this.lastMsg = lastMsg;
        this.time = time;
    }
}
