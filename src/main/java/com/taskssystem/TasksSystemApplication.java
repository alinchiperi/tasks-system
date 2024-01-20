package com.taskssystem;

import com.taskssystem.model.Tag;
import com.taskssystem.model.Task;
import com.taskssystem.model.TaskStatus;
import com.taskssystem.model.User;
import com.taskssystem.repository.TaskRepository;
import com.taskssystem.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class TasksSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(TasksSystemApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(UserRepository userRepository, PasswordEncoder passwordEncoder, TaskRepository taskRepository) {
        return args -> {
            User user = userRepository.save(new User("chiperialin@yahoo.com", passwordEncoder.encode("password") ));
            LocalDateTime date = LocalDateTime.now().plusHours(5L);
            LocalDateTime date2 = LocalDateTime.now().plusHours(6L);
            List<Tag> list = new ArrayList<>();
            taskRepository.save(new Task(1, "This is a task" ,  "This is a description", date, TaskStatus.PENDING, user, list ));
            taskRepository.save(new Task(2, "This is a task2" ,  "This is a description2", date2, TaskStatus.PENDING, user, list ));

        };
    }


}
