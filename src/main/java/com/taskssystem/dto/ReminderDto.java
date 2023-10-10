package com.taskssystem.dto;

import com.taskssystem.model.Reminder;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ReminderDto {
    private Integer id;
    private Integer taskId;
    private LocalDateTime reminderDateTime;
    private boolean sent;

    public static ReminderDto from(Reminder reminder) {
        return ReminderDto.builder()
                .id(reminder.getId())
                .taskId(reminder.getTask().getId())
                .reminderDateTime(reminder.getReminderDateTime())
                .sent(reminder.isSent())
                .build();
    }
}
