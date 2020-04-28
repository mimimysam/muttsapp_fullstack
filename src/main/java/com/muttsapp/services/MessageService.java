package com.muttsapp.services;

import com.muttsapp.repositories.MessageRepository;
import com.muttsapp.tables.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    @Autowired
    MessageRepository repo;

    public Message getMessageById(int id) {
        return repo.findById(id);
    }

    public void deleteMessage(int id) {
        repo.deleteById(id);
    }

}
