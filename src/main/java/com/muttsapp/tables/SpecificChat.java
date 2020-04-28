package com.muttsapp.tables;

public class SpecificChat {

    int id;
    int chatId;
    String message;
    String dateSent;
    int senderId;
    String chatTitle;

    public SpecificChat() {
    }

    public SpecificChat(int id, int chatId, String message, String dateSent, int senderId, String chatTitle) {
        this.id = id;
        this.chatId = chatId;
        this.message = message;
        this.dateSent = dateSent;
        this.senderId = senderId;
        this.chatTitle = chatTitle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDateSent() {
        return dateSent;
    }

    public void setDateSent(String dateSent) {
        this.dateSent = dateSent;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public String getChatTitle() {
        return chatTitle;
    }

    public void setChatTitle(String chatTitle) {
        this.chatTitle = chatTitle;
    }
}
