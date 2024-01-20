package com.taskssystem.controller;

import com.taskssystem.dto.TaskDto;
import com.taskssystem.model.User;
import com.taskssystem.service.TaskService;
import com.taskssystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final UserService userService;
    private final TaskService taskService;
    private final KafkaTemplate<String, String> kafkaTemplate;


    public UserController(UserService userService, TaskService taskService, KafkaTemplate<String, String> kafkaTemplate) {
        this.userService = userService;
        this.taskService = taskService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @DeleteMapping("{id}/delete")
    public ResponseEntity<Void>delete(@PathVariable("id") Integer id){
        Optional<User> user = userService.findUserById(id);
        log.info("controller");
        if (user.isPresent() && user.get().isActive()){
            userService.deleteUser(user.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
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

    @PostMapping("{email}/premium")
    public void premiumSubscribe(@PathVariable String email){
        userService.upgradeToPremium(email);
    }
}
