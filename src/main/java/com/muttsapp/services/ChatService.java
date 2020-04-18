package com.muttsapp.services;

import com.muttsapp.mappers.UserChatMapper;
import com.muttsapp.repositories.ChatRepository;
import com.muttsapp.tables.Chat;
import com.muttsapp.tables.User;
import com.muttsapp.tables.UserChat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {

    @Autowired
    ChatRepository repo;

    @Autowired
    UserChatMapper userChatMapper;

    public List<UserChat> getChatsByUserId(int userId) {
        return userChatMapper.getChatsByUserId(userId);
    }

    public Chat getChatByChatId(int chatId) {
        return repo.findByChatId(chatId);
    }

    public void saveChat(Chat chat) {
        repo.save(chat);
    }

    public void deleteChat(int chatId) {
        repo.deleteByChatId(chatId);
    }

}
