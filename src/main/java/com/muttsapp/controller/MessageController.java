package com.muttsapp.controller;

import com.muttsapp.model.CustomResponseObject;
import com.muttsapp.services.ChatService;
import com.muttsapp.services.ImageService;
import com.muttsapp.services.MessageService;
import com.muttsapp.tables.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.io.IOException;

@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    MessageService messageService;

    @Autowired
    ChatService chatService;

    @Autowired
    ImageService imageService;

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

    @DeleteMapping("/chat/{chatId}")
    public void deleteChat(@PathVariable("chatId") int chatId){
        chatService.deleteChat(chatId);
    }

    @PostMapping("/image/{chatId}/{userId}")
    public void fileUpload(@PathVariable("chatId") int chatId,
                             @PathVariable("userId") int userId,
                             @RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) throws IOException {
        imageService.uploadFile(file, chatId, userId);
    }
}
