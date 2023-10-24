package com.taskssystem.controller;

import com.taskssystem.dto.RegisterUserDto;
import com.taskssystem.dto.TaskDto;
import com.taskssystem.exceptions.TaskNotFoundException;
import com.taskssystem.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@Slf4j
@RequestMapping("/api/task")
@CrossOrigin(origins = {"http://localhost:4200"}, maxAge = 3600)
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addTask(@RequestBody TaskDto taskDto) {
        try {
            log.info("Add");
            taskService.createTask(taskDto);
            return new ResponseEntity<>("Task successful added", HttpStatus.valueOf(200));
        } catch (Exception e) {
            log.error("Unexpected error :(");
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> findTaskById(@PathVariable Integer id) {
        try {
            TaskDto taskDtoById = taskService.findByIdDto(id);
            return new ResponseEntity<>(taskDtoById, HttpStatus.OK);
        } catch (TaskNotFoundException e) {
            log.error("Unexpected error :(");
            return new ResponseEntity<>(HttpStatus.valueOf(404));
        }
    }

    @DeleteMapping("{id}/delete")
    public ResponseEntity<Void> deleteTask(@PathVariable Integer id) {
        try {
            taskService.deleteTask(id);
            return new ResponseEntity<>(HttpStatus.valueOf(200));
        } catch (TaskNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.valueOf(404));
        }
    }

    @PatchMapping("{id}/update")
    public ResponseEntity<TaskDto> updateTask(@RequestBody TaskDto taskDto) {
        return new ResponseEntity<>(taskService.updateTask(taskDto), HttpStatus.OK);
    }

    @GetMapping("/tags")
    public ResponseEntity<Set<TaskDto>> getTaskByTags(@RequestBody List<String> tags) {
        Set<TaskDto> taskDtos = taskService.getTasksByTag(tags);
        if (taskDtos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(taskDtos);
    }

    @GetMapping("/all")
    public ResponseEntity<List<TaskDto>> getUserTask( @RequestParam String email) {

        List<TaskDto> taskForUser = taskService.getTaskForUser(email);
        if (taskForUser.isEmpty())
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.ok(taskForUser);
    }
}
