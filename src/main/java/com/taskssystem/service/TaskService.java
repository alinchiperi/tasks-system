package com.taskssystem.service;

import com.taskssystem.dto.TaskDto;
import com.taskssystem.exceptions.TaskNotFoundException;
import com.taskssystem.model.Reminder;
import com.taskssystem.model.Task;
import com.taskssystem.model.User;
import com.taskssystem.repository.ReminderRepository;
import com.taskssystem.repository.TaskRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserService userService;
    private final ReminderRepository reminderRepository;

    public TaskService(TaskRepository taskRepository, UserService userService, ReminderRepository reminderRepository) {
        this.taskRepository = taskRepository;
        this.userService = userService;
        this.reminderRepository = reminderRepository;
    }

    public void createTask(TaskDto taskDto) {
        Task task = new Task();
        User user = userService.getCurrentUser().orElseThrow(() -> new UsernameNotFoundException("You need to login"));
        task.setUser(user);
        task.setTitle(taskDto.getTitle());
        task.setDueDate(taskDto.getDueDate());
        if (taskDto.getDescription() != null) {
            task.setDescription(taskDto.getDescription());
        }

        Task taskSaved = taskRepository.save(task);


        //reminder logic
        LocalDate currentDate = LocalDate.now();
        LocalDate dueDate = taskDto.getDueDate().toLocalDate();
//        if (!dueDate.isEqual(currentDate)) {
            Reminder reminder = new Reminder();
            reminder.setReminderDateTime(task.getDueDate().minusDays(1)); // 1 day before the task's due date
            reminder.setTask(taskSaved);
            reminderRepository.save(reminder);
//        }
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
            task.setTaskStatus(newTask.getTaskStatus());
            return TaskDto.from(taskRepository.save(task));
        }
        return TaskDto.from(new Task());

    }
}
