package com.taskssystem.repository;

import com.taskssystem.model.Reminder;
import com.taskssystem.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Integer> {
    List<Reminder> findByReminderDateTimeBeforeAndSentFalseAndTaskTaskStatusNotIn(
            LocalDateTime now, List<TaskStatus> excludedStatuses);
}
