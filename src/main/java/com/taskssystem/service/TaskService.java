package com.taskssystem.service;

import com.taskssystem.dto.TaskDto;
import com.taskssystem.exceptions.TaskNotFoundException;
import com.taskssystem.model.Task;
import com.taskssystem.model.User;
import com.taskssystem.repository.TaskRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserService userService;

    public TaskService(TaskRepository taskRepository, UserService userService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
    }

    public void addTask(TaskDto taskDto) {
        Task task = new Task();
        User user = userService.getCurrentUser().orElseThrow(() -> new UsernameNotFoundException("You need to login"));
        task.setUser(user);
        task.setTitle(taskDto.getTitle());
        task.setDueDate(taskDto.getDueDate());
        if (taskDto.getDescription() != null) {
            task.setDescription(taskDto.getDescription());
        }
        taskRepository.save(task);
    }

    public TaskDto findById(Integer id) {
        Optional<Task> task = taskRepository.findById(id);
        if (task.isPresent()) {
            return TaskDto.from(task.get());
        } else {
            throw new TaskNotFoundException("Task not found");
        }

    }

    public void deleteTask(Integer id) {
        taskRepository.deleteById(id);
    }

    public TaskDto updateTask(TaskDto newTask) {
        Optional<Task> taskById = taskRepository.findById(newTask.getId());
        if (taskById.isPresent()) {
            Task task = taskById.get();
            task.setTitle(newTask.getTitle());
            task.setDescription(newTask.getDescription());
            task.setDueDate(newTask.getDueDate());
            return TaskDto.from(taskRepository.save(task));
        }
        return TaskDto.from(new Task());

    }
}
