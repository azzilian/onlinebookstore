package com.onlinebookstore.onlinebookstore.dto.user;

import com.onlinebookstore.onlinebookstore.service.impl.validator.FieldMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@FieldMatch(field = "password",
        fieldMatch = "repeatPassword",
        message = "The password fields must match")

public class UserRegistrationRequestDto {
    @NotBlank(message = "email field cannot be empty")
    @Email
    private String email;

    @NotBlank(message = "password field cannot be empty")
    @Size(min = 6, max = 20)
    private String password;

    @NotBlank(message = "repeatPassword field cannot be empty")
    @Size(min = 6, max = 20)
    private String repeatPassword;

    private String firstName;

    private String lastName;

    private String shippingAddress;
}
