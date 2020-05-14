package com.muttsapp.controller;

import com.muttsapp.model.CustomResponseObject;
import com.muttsapp.services.MessageService;
import com.muttsapp.tables.Message;
import com.muttsapp.tables.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    MessageService messageService;

    @GetMapping("/find/{id}")
    public CustomResponseObject<Message> getMessage(@PathVariable("id") int id) {
        CustomResponseObject<Message> obj = new CustomResponseObject<>();
        obj.setData(messageService.getMessageById(id));
        return obj;
    }

    @DeleteMapping("/{id}")
    public void deleteMessage(@PathVariable("id") int id){
        messageService.deleteMessage(id);
    }

}
