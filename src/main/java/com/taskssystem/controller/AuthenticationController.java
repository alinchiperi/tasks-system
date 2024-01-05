package com.taskssystem.controller;

import com.taskssystem.dto.LoginResponseDto;
import com.taskssystem.dto.RegisterUserDto;
import com.taskssystem.model.User;
import com.taskssystem.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;
import java.util.Arrays;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthenticationController {
    private final AuthenticationService authService;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public AuthenticationController(AuthenticationService authService, KafkaTemplate<String, String> kafkaTemplate) {
        this.authService = authService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping("/register")
    public User register(@RequestBody RegisterUserDto body) {
        return authService.register(body.getEmail(), body.getPassword());
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody RegisterUserDto body) {
        LoginResponseDto login = authService.login(body.getEmail(), body.getPassword());
        if (login.getToken().isEmpty()) {
            return new ResponseEntity<>(login, HttpStatus.NOT_FOUND);
        } else {
            kafkaTemplate.send("tasks-system", "user with email: " + body.getEmail() + " login");
            return new ResponseEntity<>(login, HttpStatus.OK);
        }
    }
}
