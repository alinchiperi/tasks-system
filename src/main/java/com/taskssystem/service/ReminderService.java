package com.taskssystem.service;

import com.taskssystem.dto.ReminderDto;
import com.taskssystem.model.Reminder;
import com.taskssystem.model.TaskStatus;
import com.taskssystem.repository.ReminderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReminderService {
    private final ReminderRepository reminderRepository;

    public ReminderService(ReminderRepository reminderRepository) {
        this.reminderRepository = reminderRepository;
    }


    public List<ReminderDto> dueReminders() {
        LocalDateTime now = LocalDateTime.now();
        List<TaskStatus> excludedStatuses = Arrays.asList(TaskStatus.CANCELED, TaskStatus.COMPLETED);

        List<Reminder> dueReminders = reminderRepository.findByReminderDateTimeBeforeAndSentFalseAndTaskTaskStatusNotIn(now, excludedStatuses);
        return dueReminders.stream().map(ReminderDto::from).collect(Collectors.toList());
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
}
