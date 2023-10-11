package com.taskssystem.repository;

import com.taskssystem.model.Tag;
import com.taskssystem.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findByTags_Name(String tagName);
    List<Task> findByTags_NameIn(List<String> tags);
}
