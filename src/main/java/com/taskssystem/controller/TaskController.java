package com.taskssystem.controller;

import com.taskssystem.dto.TaskDto;
import com.taskssystem.model.Task;
import com.taskssystem.model.User;
import com.taskssystem.service.TaskService;
import com.taskssystem.service.UserService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/task")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addTask(@RequestBody TaskDto taskDto) {
        try {
            log.info("Add");
            taskService.addTask(taskDto);
            return new ResponseEntity<>(HttpStatus.valueOf(200));
        } catch (Exception e) {
            log.error("Unexpected error :(");
            return new ResponseEntity<>(HttpStatus.valueOf(422));
        }
    }
    @GetMapping("/{id}")
    public TaskDto findTaskById(@PathVariable Integer id){
        return taskService.findById(id);
    }
}
