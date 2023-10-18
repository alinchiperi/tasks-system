package com.taskssystem.service;

import com.taskssystem.dto.LoginResponseDto;
import com.taskssystem.model.User;
import com.taskssystem.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, TokenService tokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    public User register(String email, String password) {
        String encodedPass = passwordEncoder.encode(password);

        return userRepository.save(new User(email, encodedPass));
    }

    public LoginResponseDto login(String email, String password) {
        try {

            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            String token = tokenService.generateJwt(auth);
            return new LoginResponseDto(token);
        } catch (AuthenticationException e) {
            log.info("catch clause ");
            log.error("error is " + e.getMessage());
            return new LoginResponseDto("");
        }
    }
}
