package com.muttsapp.mappers;

import com.muttsapp.tables.UserChat;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserChatMapper {

    String query = "select distinct(c.chat_title) as chat_name, c.id as chat_id, " +
            "m.userId as senderId " +
            "from chats c " +
            "join messages m " +
            "on m.chatId = c.id " +
            "join usersChats uc " +
            "on uc.chatId = c.id " +
            "where uc.userId = #{userId} " +
            "and m.userId != #{userId} " +
            "group by chatId, senderId " +
            "order by m.dateSent asc";

    @Select(query)
    public List<UserChat> getChatsByUserId(int userId);

}
