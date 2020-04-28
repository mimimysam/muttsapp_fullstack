package com.muttsapp.repositories;

import com.muttsapp.tables.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Integer> {

    Chat findByChatId(int chatId);

    void deleteByChatId(int chatId);

}
