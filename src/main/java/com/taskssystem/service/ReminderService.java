package com.taskssystem.service;

import com.taskssystem.dto.ReminderDto;
import com.taskssystem.exceptions.ReminderNotFoundException;
import com.taskssystem.exceptions.TaskNotFoundException;
import com.taskssystem.model.Reminder;
import com.taskssystem.model.Task;
import com.taskssystem.model.TaskStatus;
import com.taskssystem.model.User;
import com.taskssystem.repository.ReminderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class ReminderService {
    private final ReminderRepository reminderRepository;
    private final TaskService taskService;
    private final UserService userService;

    public ReminderService(ReminderRepository reminderRepository, TaskService taskService, UserService userService) {
        this.reminderRepository = reminderRepository;
        this.taskService = taskService;
        this.userService = userService;
    }


    public List<ReminderDto> dueReminders() {
        LocalDateTime now = LocalDateTime.now();
        List<TaskStatus> excludedStatuses = Arrays.asList(TaskStatus.CANCELED, TaskStatus.COMPLETED);

//        List<Reminder> dueReminders = reminderRepository.findByReminderDateTimeBeforeAndSentFalseAndTaskTaskStatusNotIn(now, excludedStatuses);
        List<Reminder> dueReminders = reminderRepository.findAll();

        Optional<User> currentUser = userService.getCurrentUser();
        List<Reminder> userReminder = new ArrayList<>();
        if (currentUser.isPresent()) {
            int userId = currentUser.get().getId();
            userReminder = dueReminders.stream().filter(r -> (r.getTask().getUser().getId() == userId))
                    .filter(r -> !r.isSent())
                    .toList();
        }
        return userReminder.stream().map(ReminderDto::from).collect(toList());
    }

    public List<Reminder> getAllReminders() {
        return reminderRepository.findAll();
    }

    public boolean deleteReminder(Integer id) {
        if (reminderRepository.existsById(id)) {
            reminderRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Reminder getReminderById(Integer id) {
        return reminderRepository.findById(id).orElse(null);
    }

    public Reminder addReminder(ReminderDto reminderDto) {
        log.info("this is a reminder " + reminderDto);
        Optional<Task> task = taskService.getById(reminderDto.getTaskId());
        if (task.isPresent()) {
            Reminder reminder = new Reminder();
            reminder.setReminderDateTime(reminderDto.getReminderDateTime());
            reminder.setTask(task.get());
            reminder.setSent(false);
            return reminderRepository.save(reminder);
        } else {
            throw new TaskNotFoundException("Task id not found");
        }
    }

    public Reminder updateReminder(ReminderDto reminderDto) {
        Reminder reminderById = reminderRepository.findById(reminderDto.getId()).orElseThrow(ReminderNotFoundException::new);
        reminderById.setReminderDateTime(reminderDto.getReminderDateTime());
        //// TODO: 23.10.2023 add if is need
        return reminderRepository.save(reminderById);
    }
}
