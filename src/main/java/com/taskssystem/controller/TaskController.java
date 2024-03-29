package com.taskssystem.controller;

import com.taskssystem.dto.TaskDto;
import com.taskssystem.exceptions.TaskNotFoundException;
import com.taskssystem.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
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
    private final KafkaTemplate<String, String> kafkaTemplate;


    public TaskController(TaskService taskService, KafkaTemplate<String, String> kafkaTemplate) {
        this.taskService = taskService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping("/add")
    public ResponseEntity<TaskDto> addTask(@RequestBody TaskDto taskDto) {
        try {
            log.info("Add");
            TaskDto task = taskService.createTask(taskDto);
            return new ResponseEntity<>(task, HttpStatus.valueOf(200));
        } catch (Exception e) {
            log.error("Unexpected error :(  " + e.getMessage());
            return new ResponseEntity<>(TaskDto.builder().build(), HttpStatus.BAD_REQUEST);
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
    public ResponseEntity<List<TaskDto>> getUserTask(@RequestParam String email) {

        List<TaskDto> taskForUser = taskService.getUncompletedTaskForUser(email);
        if (taskForUser.isEmpty())
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.ok(taskForUser);
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<TaskDto> completeTask(@PathVariable Integer id) {
        try {
            TaskDto taskDto = taskService.completeTask(id);
            kafkaTemplate.send("tasks-system", "task with id " + id + " was completed");
            return new ResponseEntity<>(taskDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(TaskDto.builder().build(), HttpStatus.BAD_REQUEST);
        }
    }
}
