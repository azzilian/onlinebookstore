package com.onlinebookstore.onlinebookstore.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;;

@Data
public class UserRegistrationRequestDto {
    @NotBlank(message = "email field cannot be empty")
    @Email
    private String email;

    @NotBlank(message = "password field cannot be empty")
    private String password;

    @NotBlank
    private String repeatpassword;

    private String firstname;

    private String lastname;

    private String shippingaddress;
}
