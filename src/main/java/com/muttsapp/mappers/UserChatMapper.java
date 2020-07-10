package com.muttsapp.mappers;

import com.muttsapp.tables.Message;
import com.muttsapp.tables.SpecificChat;
import com.muttsapp.tables.UserChat;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.ArrayList;
import java.util.List;

@Mapper
public interface UserChatMapper {

    String GET_CHATS_BY_USER_ID = "SELECT distinct(c.chatTitle) as chatName, c.chatId, max(m.dateSent) as dateSent FROM chat c " +
            "join userChat uc on c.chatId = uc.chatId " +
            "left join message m on m.chatId = uc.chatId " +
            "where uc.userId = #{userId} " +
            "group by chatId " +
            "order by dateSent desc";

    String GET_LAST_MESSAGE = "select * from message where chatid = #{chatId} " +
            "order by id desc limit 1";

    String GET_PHOTO_URL = "select photoUrl from user where userId = #{userId}";

    String GET_SPECIFIC_CHATS_BY_ID = "select c.chatId, m.message, m.dateSent, m.chatId, m.userId, c.chatTitle " +
            "from message m " +
            "join chat c " +
            "on c.chatId = m.chatId " +
            "where m.userId = #{userId} and c.chatId = #{chatId} " +
            "order by id desc";

    String FIND_MESSAGES_BY_CHAT_ID = "select c.chatId, m.id, m.message, m.isImage, m.dateSent, m.chatId, m.userId, c.chatTitle " +
            "from message m " +
            "join chat c " +
            "on c.chatId = m.chatId " +
            "where c.chatId = #{chatId} " +
            "order by m.dateSent asc";

    String SELECT_CHAT_ID_BY_CHAT_TITLE = "select chatId from chat where chatTitle = #{chatTitle}";

    String GET_CHAT_ID_BY_USER_IDS = "select uc.chatId from userChat uc where uc.userId = #{param1} " +
            "or uc.userId = #{param2} " +
            "group by uc.chatId " +
            "order by count(uc.chatId) " +
            "desc limit 1";

    String SAVE_MESSAGE = "INSERT INTO `MuttsApp`.`message` (`message`, `chatId`, `userId`, `isImage`) VALUES (#{message}, " +
            "#{chatId}, #{userId}, #{isImage})";

    String GET_OTHER_USER_ID = "SELECT uc2.userId " +
            "FROM userChat uc1 " +
            "JOIN userChat uc2 ON uc1.chatId = uc2.chatId AND uc1.userId != uc2.userId " +
            "WHERE uc1.userId = #{param1} AND uc1.chatId = #{param2}";

    String CREATE_NEW_CHAT = "INSERT INTO MuttsApp.chat (chatTitle) VALUES (#{chatTitle})";

    String UPDATE_USER_CHATS = "insert into `MuttsApp`.userChat (userId, chatId) VALUES (#{param1}, #{param2})";

    String FIND_MESSAGE =  "SELECT m.id, m.message, m.dateSent, m.chatId, m.userId FROM message m WHERE (id = #{id})";

    String DELETE_MESSAGE = "DELETE FROM `MuttsApp`.`message` WHERE (id = #{id})";

    String DELETE_CHAT = "DELETE FROM `MuttsApp`.`chat` WHERE (`chatId` = #{chatId})";

    String DELETE_USER_CHAT = "DELETE FROM MuttsApp.userChat where chatId = #{chatId}";

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
    int getChatIdByUserIds(int userId, int otherUserId);

    @Insert(SAVE_MESSAGE)
    void saveMessage(Message msg);

    @Select(GET_OTHER_USER_ID)
    public int getOtherUserId(int userId, int chatId);

    @Insert(CREATE_NEW_CHAT)
    int createNewChat(String chatTitle);

    @Insert(UPDATE_USER_CHATS)
    void updateUserChats(int userId, int chatId);

    @Select(FIND_MESSAGE)
    public Message findMessage(int id);

    @Delete(DELETE_MESSAGE)
    void deleteMessage(int id);

    @Delete(DELETE_CHAT)
    void deleteChat(int chatId);

    @Delete(DELETE_USER_CHAT)
    void deleteUserChat(int chatId);

}
