package com.taskssystem.controller;

import com.taskssystem.dto.ReminderDto;
import com.taskssystem.model.Reminder;
import com.taskssystem.service.ReminderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/reminders")
public class ReminderController {
    private final ReminderService reminderService;

    public ReminderController(ReminderService reminderService) {
        this.reminderService = reminderService;
    }

    @PostMapping("/add")
    public ResponseEntity<ReminderDto> addReminder(@RequestBody ReminderDto reminderDto) {
        Reminder reminder = reminderService.addReminder(reminderDto);
        try {
            ReminderDto reminderDto1 = ReminderDto.from(reminder);
            return new ResponseEntity<>(reminderDto1, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/all")
    public ResponseEntity<List<ReminderDto>> dueReminders() {
        List<ReminderDto> reminders = reminderService.dueReminders();
        return new ResponseEntity<>(reminders, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReminderDto> getReminderById(@PathVariable Integer id) {
        Reminder reminder = reminderService.getReminderById(id);
        if (reminder != null) {
            return new ResponseEntity<>(ReminderDto.from(reminder), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteReminder(@PathVariable Integer id) {
        boolean deleted = reminderService.deleteReminder(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/update")
    public ResponseEntity<ReminderDto> updateReminder(@RequestBody ReminderDto reminderToUpdate) {
        try {
            log.info("Reminder to update " + reminderToUpdate);
            Reminder updatedReminder = reminderService.updateReminder(reminderToUpdate);

            return new ResponseEntity<>(ReminderDto.from(updatedReminder), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
