package com.taskssystem.service;

import com.taskssystem.dto.TaskDto;
import com.taskssystem.exceptions.TaskNotFoundException;
import com.taskssystem.model.Reminder;
import com.taskssystem.model.Tag;
import com.taskssystem.model.Task;
import com.taskssystem.model.User;
import com.taskssystem.repository.ReminderRepository;
import com.taskssystem.repository.TaskRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserService userService;
    private final ReminderRepository reminderRepository;
    private final TagService tagService;


    public TaskService(TaskRepository taskRepository, UserService userService, ReminderRepository reminderRepository, TagService tagService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
        this.reminderRepository = reminderRepository;
        this.tagService = tagService;
    }

    public TaskDto createTask(TaskDto taskDto) {
        Task task = new Task();
        User user = userService.findUserById(taskDto.getUserId()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        task.setUser(user);
        task.setTitle(taskDto.getTitle());
        task.setDueDate(taskDto.getDueDate());
        if (taskDto.getDescription() != null) {
            task.setDescription(taskDto.getDescription());
        }
        List<Tag> tags = tagService.tagsFrom(taskDto.getTags());

        task.setTags(tags);

        Task taskSaved = taskRepository.save(task);
        return TaskDto.from(taskSaved);

/*
        //reminder logic
        //// TODO: 23.10.2023 Create logic for automatically reminders
        LocalDate currentDate = LocalDate.now();
        LocalDate dueDate = taskDto.getDueDate().toLocalDate();
//        if (!dueDate.isEqual(currentDate)) {
        Reminder reminder = new Reminder();
        reminder.setReminderDateTime(task.getDueDate().minusDays(1)); // 1 day before the task's due date
        reminder.setTask(taskSaved);
        reminderRepository.save(reminder);
//        }
*/
    }

    public TaskDto findByIdDto(Integer id) {
        Optional<Task> task = getById(id);
        if (task.isPresent()) {
            return TaskDto.from(task.get());
        } else {
            throw new TaskNotFoundException("Task not found");
        }

    }

    public Optional<Task> getById(Integer id) {
        return taskRepository.findById(id);
    }


    public void deleteTask(Integer id) {
        taskRepository.deleteById(id);
    }

    public TaskDto updateTask(TaskDto newTask) {
        Optional<Task> taskById = getById(newTask.getId());
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

    public Set<TaskDto> getTasksByTag(List<String> tags) {
        List<Task> byTagsNameIn = taskRepository.findByTags_NameIn(tags);
        return byTagsNameIn.stream()
                .map(TaskDto::from)
                .collect(Collectors.toSet());
    }

    //// TODO: 23.10.2023 Add @Scheduled and change email for sending
    public List<TaskDto> getTasksForToday(User user) {
        LocalDate today = LocalDate.now();
        List<Task> tasksForUserAndDate = taskRepository.findTasksForUserAndDate(user.getId(), today);
        return listToDto(tasksForUserAndDate);
    }

    public List<TaskDto> getTaskForUser(String email){
        List<Task> byUserEmail = taskRepository.findByUserEmail(email);
        return listToDto(byUserEmail);
    }

    private static List<TaskDto> listToDto(List<Task> taskList) {
        return taskList.stream().map(TaskDto::from).collect(Collectors.toList());
    }
}
