package com.muttsapp.tables;

import javax.persistence.*;

@Entity
@Table(name="chat")
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int chatId;
    String chatTitle;
    String createdAt;

    public Chat() {
    }

    public int getId() {
        return chatId;
    }

    public void setId(int id) {
        this.chatId = id;
    }

    public String getChatTitle() {
        return chatTitle;
    }

    public void setChatTitle(String chatTitle) {
        this.chatTitle = chatTitle;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

}
