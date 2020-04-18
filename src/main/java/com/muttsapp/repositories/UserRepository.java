package com.muttsapp.repositories;

import com.muttsapp.tables.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUserId(int userId);

    User findByFirstNameAndLastName(String firstName, String lastName);

    User findByEmail(String email);

    void deleteByUserId(int userId);

}
