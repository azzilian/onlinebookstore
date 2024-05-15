package com.onlinebookstore.onlinebookstore.controller;

import com.onlinebookstore.onlinebookstore.dto.user.UserLoginRequestDto;
import com.onlinebookstore.onlinebookstore.dto.user.UserLoginResponseDto;
import com.onlinebookstore.onlinebookstore.dto.user.UserRegistrationRequestDto;
import com.onlinebookstore.onlinebookstore.dto.user.UserResponseDto;
import com.onlinebookstore.onlinebookstore.exeption.ValidationException;
import com.onlinebookstore.onlinebookstore.service.interfaces.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User management", description = "Endpoints for managing users")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/auth")
public class AuthenticationController {
    private final UserService userService;

    @PostMapping("/registration")
    @PreAuthorize("permitAll()")
    @Operation(summary = "Create new User",
            description = "Create new User in DB")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto register(@Valid @RequestBody UserRegistrationRequestDto requestDto)
            throws ValidationException {
        return userService.register(requestDto);
    }

    @PostMapping("/login")
    @PreAuthorize("permitAll()")
    public UserLoginResponseDto login(UserLoginRequestDto requestDto) {

    }
}
