package com.muttsapp.tables;

import javax.persistence.Entity;
import javax.persistence.Table;

public class UserChat {

    String chatName;
    int chatId;
    String lastMessage;
    int senderId;
    int otherUserId;
    String dateSent;
    String photoUrl;

    public UserChat() {
    }

    public UserChat(String chatName, int chatId, String lastMessage, int senderId, int otherUserId, String dateSent, String photoUrl) {
        this.chatName = chatName;
        this.chatId = chatId;
        this.lastMessage = lastMessage;
        this.senderId = senderId;
        this.otherUserId = otherUserId;
        this.dateSent = dateSent;
        this.photoUrl = photoUrl;
    }

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
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

    public int getOtherUserId() {
        return otherUserId;
    }

    public void setOtherUserId(int otherUserId) {
        this.otherUserId = otherUserId;
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
