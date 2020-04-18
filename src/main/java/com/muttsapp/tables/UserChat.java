package com.muttsapp.tables;

public class UserChat {

    String chatName;
    int chatId;
    String lastMessage;
    int senderId;
    String dateSent;
    String photoUrl;

    public UserChat() {
    }

    public UserChat(String chatName, int chatId, String lastMessage, int senderId, String dateSent, String photoUrl) {
        this.chatName = chatName;
        this.chatId = chatId;
        this.lastMessage = lastMessage;
        this.senderId = senderId;
        this.dateSent = dateSent;
        this.photoUrl = photoUrl;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public String getDateSent() {
        return dateSent;
    }

    public void setDateSent(String dateSent) {
        this.dateSent = dateSent;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
