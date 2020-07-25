package com.muttsapp.services;

import com.muttsapp.mappers.UserMapper;
import com.muttsapp.repositories.RoleRepository;
import com.muttsapp.tables.Role;
import com.muttsapp.tables.User;
import com.muttsapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Service
public class UserLoginService {

    @Autowired
    UserRepository repo;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserMapper userMapper;

    @Autowired
    AmazonSESService amazonSESService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public List<User> getAllUsers() {
        return repo.findAll();
    }

    public void saveUser(User user) throws IOException {
        user.setEnabled(true);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Role userRole = roleRepository.findByRole("USER");
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        repo.save(user);
        String userName = userMapper.getFirstName(user.getUserId()) + " " + userMapper.getLastName(user.getUserId());
        amazonSESService.sendNewUserNotification(userName);
    }

    public User findUserByID (int userId) {
        return repo.findByUserId(userId);
    }

    public User findUserByUserName(String userName) {
        return repo.findByUserName(userName);
    }

    public void deleteUserId(int userId) {
        repo.deleteByUserId(userId);
    }

    public User findByEmail(String email) {
        return repo.findByEmail(email);
    }
}
