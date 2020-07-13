package com.muttsapp.mappers;

import com.muttsapp.tables.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.ArrayList;

@Mapper
public interface UserMapper {

    String SELECT_ALL_USERS = "select * from `MuttsApp`.user";

    String SELECT_BY_FIRST_LAST_NAME = "select * from `MuttsApp`.user " +
            "where firstName = #{param1} and lastName = #{param2}";

    String GET_USER_FIRST_NAMES = "SELECT firstName FROM MuttsApp.user " +
            "where userId = #{param1} or userId = #{param2}";

    String INSERT_USER = "INSERT INTO `MuttsApp`.user (firstName, lastName) " +
            "VALUES (#{firstName}, #{lastName})";

    String UPDATE_USER = "UPDATE `MuttsApp`.`user` SET `firstName` = #{firstName}, " +
            "`lastName` = #{lastName}, `isActive` = #{isActive} WHERE (`userId` = #{userId})";

    String DELETE_USER = "UPDATE `MuttsApp`.user set isActive = false where userId = #{userId}";

    String GET_FIRST_NAME = "SELECT firstName FROM MuttsApp.user where userId = #{userId}";

    String GET_LAST_NAME = "SELECT lastName FROM MuttsApp.user where userId = #{userId}";

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
    int makeUserInactive(int userId);

    @Select(GET_FIRST_NAME)
    public String getFirstName(int userId);

    @Select(GET_LAST_NAME)
    public String getLastName(int userId);

}