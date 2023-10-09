package com.taskssystem.dto;

import com.taskssystem.model.Task;
import com.taskssystem.model.TaskStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TaskDto {
    private Integer id;
    private String title;
    private String description;
    private TaskStatus taskStatus;
    private LocalDateTime dueDate;
    private Integer userId;
    public static TaskDto from(Task task){
        return TaskDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .dueDate(task.getDueDate())
                .taskStatus(task.getTaskStatus())
                .userId(task.getUser().getId())
                .build();
    }

}
