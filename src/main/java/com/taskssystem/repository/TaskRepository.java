package com.taskssystem.repository;

import com.taskssystem.model.Tag;
import com.taskssystem.model.Task;
import com.taskssystem.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findByTags_NameIn(List<String> tags);
    @Query("SELECT t FROM Task t WHERE t.user.id = :userId AND DATE(t.dueDate) = :dueDate")
    List<Task> findTasksForUserAndDate(@Param("userId") Integer userId, @Param("dueDate") LocalDate dueDate);

    List<Task> findByUserEmail(String email);
    @Query("SELECT t FROM Task t WHERE t.user.id = :userId AND t.taskStatus NOT IN (:excludedStatuses)")
    List<Task> findTasksForUserExcludingStatuses(@Param("userId") Integer userId, @Param("excludedStatuses") List<TaskStatus> excludedStatuses);


}
