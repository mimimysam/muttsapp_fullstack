package com.muttsapp.controller;

import com.muttsapp.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/message")
public class MessageController {

    @Autowired
    MessageService messageService;

    @RequestMapping("/delete/{id}")
    public String deleteMessage(@PathVariable(name = "id") int id){
        messageService.deleteMessage(id);
        return "redirect:/index";
    }

}
