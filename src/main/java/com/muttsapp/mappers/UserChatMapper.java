package com.muttsapp.mappers;

import com.muttsapp.tables.Message;
import com.muttsapp.tables.SpecificChat;
import com.muttsapp.tables.UserChat;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import javax.swing.plaf.PanelUI;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.List;

@Mapper
public interface UserChatMapper {

    String GET_CHATS_BY_USER_ID = "select distinct(c.chatTitle) as chatName, c.chatId, m.dateSent, " +
            "m.userId as senderId " +
            "from chat c " +
            "join message m " +
            "on m.chatId = c.chatId " +
            "join userChat uc " +
            "on uc.chatId = c.chatId " +
            "where uc.userId = #{userId} " +
            "and m.userId != #{userId} " +
            "group by chatId, senderId ";

    String GET_LAST_MESSAGE = "select * from message where chatid = #{chatId} " +
            "order by id desc limit 1";

    String GET_PHOTO_URL = "select photoUrl from user where userId = #{userId}";

    String GET_SPECIFIC_CHATS_BY_ID = "select c.chatId, m.message, m.dateSent, m.chatId, m.userId, c.chatTitle " +
            "from message m " +
            "join chat c " +
            "on c.chatId = m.chatId " +
            "where m.userId = #{userId} and c.chatId = #{chatId} " +
            "order by id desc";

    String FIND_MESSAGES_BY_CHAT_ID = "select c.chatId, m.id, m.message, m.dateSent, m.chatId, m.userId, c.chatTitle " +
            "from message m " +
            "join chat c " +
            "on c.chatId = m.chatId " +
            "where c.chatId = #{chatId} " +
            "order by m.dateSent asc";

    String SELECT_CHAT_ID_BY_CHAT_TITLE = "select chatId from chats where chatTitle = #{chatTitle}";

    String GET_CHAT_ID_BY_USER_IDS = "select uc.chatId from userChat uc where uc.userId = #{param1} " +
            "or uc.userId = #{param2} " +
            "group by uc.chatId " +
            "order by count(uc.chatId) " +
            "desc limit 1";

    String SAVE_MESSAGE = "INSERT INTO `MuttsApp`.`message` (`message`, `chatId`, `userId`) VALUES (#{message}, " +
            "#{chatId}, #{userId})";

//    String CREATE_NEW_CHAT = "insert into `MuttsApp`.chat (chatTitle) " +
//            "VALUES (#{chatTitle})";

//    String UPDATE_USER_CHATS = "insert into `MuttsApp`.userChat (userId, chatId) VALUES (#{param1}, #{param2})";

    @Select(GET_CHATS_BY_USER_ID)
    public List<UserChat> getChatsByUserId(int userId);

    @Select(GET_LAST_MESSAGE)
    public Message getLastMessage(int chatId);

    @Select(GET_PHOTO_URL)
    public String getPhotoUrl(int userId);

    @Select(GET_SPECIFIC_CHATS_BY_ID)
    public ArrayList<Message> getSpecificChatsById(int userId, int chatId);

    @Select(FIND_MESSAGES_BY_CHAT_ID)
    public ArrayList<Message> findMessagesByChatId(int chatId);

    @Select(SELECT_CHAT_ID_BY_CHAT_TITLE)
    public int selectChatIdByChatTitle(String chatTitle);

    @Select(GET_CHAT_ID_BY_USER_IDS)
    int getChatIdByUserIds(long userId, long otherUserId);

    @Insert(SAVE_MESSAGE)
    void saveMessage(Message msg);

//    @Insert(CREATE_NEW_CHAT)
//    int createNewChat(String chatTitle);

//    @Insert(UPDATE_USER_CHATS)
//    void updateUserChats(int userId, int chatId);

}
