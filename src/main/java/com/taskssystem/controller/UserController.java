package com.taskssystem.controller;

import com.taskssystem.model.User;
import com.taskssystem.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/dummy")
    public User getCurrentUser(){
        return userService.getCurrentUser().orElse(new User());
    }

    @GetMapping("/current")
    public String dummyTest() {
        return userService.getCurrentUserName();
    }
}
