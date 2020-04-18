package com.muttsapp.services;

import com.muttsapp.tables.User;
import com.muttsapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserLoginService {

    @Autowired
    UserRepository repo;

    public List<User> getAllUsers() {
        return repo.findAll();
    }

    public User saveUser(User user) {
        repo.save(user);
        return repo.findByFirstNameAndLastName(user.getFirstName(), user.getLastName());
    }

    public User getUserByID (int userId) {
        return repo.findByUserId(userId);
    }

    public User findUserByEmail(String email) {
        return repo.findByEmail(email);
    }

    public void deleteUserId(int userId) {
        repo.deleteByUserId(userId);
    }

}
