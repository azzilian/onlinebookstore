package com.onlinebookstore.onlinebookstore.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserLoginRequestDto(
        @NotBlank (message = "email field cannot be empty")
        @Email
        String email,
        @NotBlank (message = "password field cannot be empty")
        @Size(min = 6, max = 20)
        String password
) {
}
