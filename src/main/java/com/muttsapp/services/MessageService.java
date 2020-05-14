package com.muttsapp.services;

import com.muttsapp.mappers.UserChatMapper;
import com.muttsapp.repositories.MessageRepository;
import com.muttsapp.tables.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    @Autowired
    UserChatMapper userChatMapper;

    public Message getMessageById(int id) {
        return userChatMapper.findMessage(id);
    }

    public void deleteMessage(int id) {
        userChatMapper.deleteMessage(id);
    }

}
