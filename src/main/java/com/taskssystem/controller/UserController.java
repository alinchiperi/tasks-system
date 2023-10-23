package com.taskssystem.controller;

import com.taskssystem.dto.TaskDto;
import com.taskssystem.model.Task;
import com.taskssystem.model.User;
import com.taskssystem.service.TaskService;
import com.taskssystem.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final TaskService taskService;

    public UserController(UserService userService, TaskService taskService) {
        this.userService = userService;
        this.taskService = taskService;
    }
    @GetMapping("/dummy")
    public User getCurrentUser(){
        return userService.getCurrentUser().orElse(new User());
    }

    @GetMapping("/current")
    public String dummyTest() {
        return userService.getCurrentUserName();
    }

    @GetMapping("/today")
    public List<TaskDto> getTodayTasks(){
        Optional<User> currentUser = userService.getCurrentUser();
        if (currentUser.isPresent())
            return taskService.getTasksForToday(currentUser.get());
        else
            return new ArrayList<>();
    }
}
