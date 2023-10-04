package com.taskssystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class TasksSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(TasksSystemApplication.class, args);
	}

}
