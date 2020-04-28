package com.muttsapp.repositories;

import com.muttsapp.tables.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUserId(int userId);

    User findByFirstNameAndLastName(String firstName, String lastName);

    User findByUserName(String userName);

    void deleteByUserId(int userId);

    User findByEmail(String email);

}
