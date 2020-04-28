package com.muttsapp.mappers;

import com.muttsapp.tables.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.ArrayList;

@Mapper
public interface UserMapper {

    String SELECT_ALL_USERS = "select * from `MuttsApp`.users";

    String SELECT_BY_FIRST_LAST_NAME = "select * from `MuttsApp`.users " +
            "where firstName = #{param1} and lastName = #{param2}";

    String GET_USER_FIRST_NAMES = "SELECT firstName FROM whatsapp.user " +
            "where userId = #{param1} or userId = #{param2}";

    String INSERT_USER = "INSERT INTO `MuttsApp`.users (firstName, lastName) " +
            "VALUES (#{firstName}, #{lastName})";

    String UPDATE_USER = "UPDATE `MuttsApp`.`users` SET `firstName` = #{firstName}, " +
            "`lastName` = #{lastName}, `isActive` = #{isActive} WHERE (`userId` = #{userId})";

    String DELETE_USER = "UPDATE `MuttsApp`.users set isActive = false where userId = #{userId} ";

    @Select(SELECT_ALL_USERS)
    public ArrayList<User> getAllUsers();

    @Select(SELECT_BY_FIRST_LAST_NAME)
    User findUserByFirstNameLastName(String firstName, String lastName);

    @Select(GET_USER_FIRST_NAMES)
    public ArrayList<String> getUserFirstNames(int userId, int otherUserId);

    @Insert(INSERT_USER)
    public int insertUser(User user);

    @Update(UPDATE_USER)
    int patchUser(User user);

    @Update(DELETE_USER)
    int makeUserInactive(int id);

}