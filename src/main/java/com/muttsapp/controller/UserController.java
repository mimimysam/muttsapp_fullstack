package com.muttsapp.controller;

import com.muttsapp.repositories.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class UserController {

    @RequestMapping("/users")
    public User getUser() {
        User user = new User();
        return user;
    }
}
