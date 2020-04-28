package com.muttsapp.repositories;

import com.muttsapp.tables.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

    Message findById(int id);

    void deleteById(int id);

}
