package com.muttsapp.controller;

import com.muttsapp.NewMessageException;
import com.muttsapp.model.CustomResponseObject;
import com.muttsapp.services.ChatService;
import com.muttsapp.services.UserLoginService;
import com.muttsapp.tables.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserLoginService userLoginService;
    @Autowired
    ChatService chatService;

    @Autowired
    public UserController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    //    private static final String WS_MESSAGE_TRANSFER_DESTINATION = "/message/alert/{userid}";
    private final SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping()
    public CustomResponseObject<List<User>> getAllUsers() {
        CustomResponseObject<List<User>> obj = new CustomResponseObject();
        obj.setData(userLoginService.getAllUsers());
        return obj;
    }

    @GetMapping("/{userId}")
    public CustomResponseObject<User> findUserById(@PathVariable("userId") int userId) {
        CustomResponseObject<User> obj = new CustomResponseObject<>();
        obj.setData(userLoginService.findUserByID(userId));
        return obj;
    }

    @GetMapping("/{userId}/chats")
    public CustomResponseObject<List<UserChat>> findChatsByUserId(@PathVariable("userId") int userId) {
        CustomResponseObject<List<UserChat>> obj = new CustomResponseObject<>();
        obj.setData(chatService.getChatsByUserId(userId));
        return obj;
    }

    @GetMapping("/{userId}/chats/{otherUserId}")
    public CustomResponseObject<ArrayList<Message>> getSpecificChatsById(@PathVariable("userId") int userId,
                                                                         @PathVariable("otherUserId") int otherUserId){
        CustomResponseObject<ArrayList<Message>> obj = new CustomResponseObject<>();
        obj.setData(chatService.getSpecificChatsById(userId, otherUserId));
        return obj;
    }

    @PostMapping("/{userId}/chats/{otherUserId}")
    public CustomResponseObject<List<UserChat>> createNewChat(
            @PathVariable("userId") int userId,
            @PathVariable("otherUserId") int otherUserId) throws NewMessageException {
        CustomResponseObject<List<UserChat>> obj = new CustomResponseObject<>();
        // creating new chat with the two users
        chatService.createNewChat(userId, otherUserId);
        obj.setData(chatService.getChatsByUserId(userId));
        return obj;
    }

    @PostMapping("/{userId}/message")
    public CustomResponseObject<List<UserChat>> insertNewMessage (
            @PathVariable("userId") int userId,
            @RequestBody Message msg) throws NewMessageException {
        CustomResponseObject<List<UserChat>> obj = new CustomResponseObject<>();
        chatService.saveMessage(msg);
        obj.setData(chatService.getChatsByUserId(userId));

        simpMessagingTemplate.convertAndSend("/topic/messages/" + obj.getData().get(0).getOtherUserId(),
                obj.getData().get(0).getLastMessage());
        return obj;
    }

}


