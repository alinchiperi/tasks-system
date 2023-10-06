package com.taskssystem.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RegisterUserDto {
    private String email;
    private String password;
}
