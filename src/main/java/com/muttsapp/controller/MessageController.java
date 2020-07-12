package com.muttsapp.controller;

import com.muttsapp.model.CustomResponseObject;
import com.muttsapp.services.ChatService;
import com.muttsapp.services.ImageService;
import com.muttsapp.services.MessageService;
import com.muttsapp.tables.Message;
import com.muttsapp.tables.UserChat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    MessageService messageService;

    @Autowired
    ChatService chatService;

    @Autowired
    ImageService imageService;

    @Autowired
    public MessageController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    private final SimpMessagingTemplate simpMessagingTemplate;

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
    public CustomResponseObject<List<UserChat>> fileUpload(@PathVariable("chatId") int chatId,
                             @PathVariable("userId") int userId,
                             @RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) throws IOException {
        CustomResponseObject<List<UserChat>> obj = new CustomResponseObject<>();
        imageService.uploadFile(file, chatId, userId);
        obj.setData(chatService.getChatsByUserId(userId));

        simpMessagingTemplate.convertAndSend("/topic/messages/" + obj.getData().get(0).getOtherUserId(),
                obj.getData().get(0).getLastMessage());
        return obj;
    }
}
