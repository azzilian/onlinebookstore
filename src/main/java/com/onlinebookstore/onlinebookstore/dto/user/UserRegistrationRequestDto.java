package com.onlinebookstore.onlinebookstore.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
public class UserRegistrationRequestDto {
    @NotBlank(message = "email field cannot be empty")
    @Email
    private String email;

    @NotBlank(message = "password field cannot be empty")
    private String password;

    @NotBlank(message = "password field cannot be empty")
    private String repeatPassword;

    private String firstName;

    private String lastName;

    private String shippingAddress;
}
