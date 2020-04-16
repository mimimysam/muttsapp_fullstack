package com.muttsapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findById(int user_id);

    User findByFirstNameLastName(String first_name, String last_name);

    User findByEmail(String email);

    void deleteById(int user_id);

}
