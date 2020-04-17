package com.muttsapp.services;

import com.muttsapp.repositories.User;
import com.muttsapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

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

    public User getUserByID (int user_id) {
        return repo.findById(user_id);
    }

    public User findUserByEmail(String email) {
        return repo.findByEmail(email);
    }

    public void deleteUser(int user_id) {
        repo.deleteById(user_id);
    }

}
