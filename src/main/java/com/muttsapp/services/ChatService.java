package com.muttsapp.services;

import com.muttsapp.NewMessageException;
import com.muttsapp.mappers.UserChatMapper;
import com.muttsapp.mappers.UserMapper;
import com.muttsapp.repositories.ChatRepository;
import com.muttsapp.tables.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatService {

    @Autowired
    ChatRepository repo;

    @Autowired
    UserChatMapper userChatMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserLoginService userLoginService;

    public List<UserChat> getChatsByUserId(int userId) {
        List<UserChat> chats = userChatMapper.getChatsByUserId(userId);
//        chats.add(userChatMapper.getChatsWithoutMessages(userId));
        for (UserChat u : chats) {
            Message m = userChatMapper.getLastMessage(u.getChatId());
            u.setLastMessage(m.getMessage());
            u.setDateSent(m.getDateSent());
            u.setOtherUserId(userChatMapper.getOtherUserId(userId, u.getChatId()));
            u.setPhotoUrl(userChatMapper.getPhotoUrl(u.getOtherUserId()));
        }
        return chats;
    }

    public ArrayList<Message> getSpecificChatsById(int userId, int otherUserId) {
        int chatId = userChatMapper.getChatIdByUserIds(userId, otherUserId);
        return userChatMapper.findMessagesByChatId(chatId);
    }

    public void createNewChat(int userId, int otherUserId) throws NewMessageException {
        // creating list of strings of the first names of the 2 users involved in this new chat
        ArrayList<String> names = userMapper.getUserFirstNames(userId, otherUserId);
        // this becomes the new chatTitle in the `chats` table
        String chatTitle = names.get(0) + " and " + names.get(1);
        // inserts this new chatTitle into the `chats` table thereby creating the new row
        // the chatId is automatically created because of the auto increment
        userChatMapper.createNewChat(chatTitle);
        // determine chatId using the title
        int chatId = userChatMapper.selectChatIdByChatTitle(chatTitle);
        // associate chatIds with both users
        userChatMapper.updateUserChats(userId, chatId);
        userChatMapper.updateUserChats(otherUserId, chatId);
    }

    public void saveMessage(Message msg) throws NewMessageException {
        userChatMapper.saveMessage(msg);
    }

}
