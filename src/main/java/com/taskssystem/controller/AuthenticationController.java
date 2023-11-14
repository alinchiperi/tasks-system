package com.taskssystem.controller;

import com.taskssystem.dto.LoginResponseDto;
import com.taskssystem.dto.RegisterUserDto;
import com.taskssystem.model.User;
import com.taskssystem.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public AuthenticationController(AuthenticationService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public User register(@RequestBody RegisterUserDto body) {
        return authService.register(body.getEmail(), body.getPassword());
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody RegisterUserDto body){
            LoginResponseDto login = authService.login(body.getEmail(), body.getPassword());
        if (login.getToken().equals("")){
            return new ResponseEntity<>(login,HttpStatus.NOT_FOUND);
        }
        else return new ResponseEntity<>(login, HttpStatus.OK);
    }
}
