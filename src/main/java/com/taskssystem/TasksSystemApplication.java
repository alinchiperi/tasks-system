package com.taskssystem;

import com.taskssystem.model.User;
import com.taskssystem.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class TasksSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(TasksSystemApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            userRepository.save(new User("chiperialin@yahoo.com", passwordEncoder.encode("password")));
        };
    }

}
