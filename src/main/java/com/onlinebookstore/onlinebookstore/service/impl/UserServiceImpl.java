package com.onlinebookstore.onlinebookstore.service.impl;

import com.onlinebookstore.onlinebookstore.dto.user.UserRegistrationRequestDto;
import com.onlinebookstore.onlinebookstore.dto.user.UserResponseDto;
import com.onlinebookstore.onlinebookstore.exeption.CustomGlobalExceptionHandler;
import com.onlinebookstore.onlinebookstore.exeption.RegistrationException;
import com.onlinebookstore.onlinebookstore.mapper.UserMapper;
import com.onlinebookstore.onlinebookstore.model.User;
import com.onlinebookstore.onlinebookstore.repository.UserRepository;
import com.onlinebookstore.onlinebookstore.service.UserService;
import com.onlinebookstore.onlinebookstore.validator.RegistrationRequestFieldMatch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

@Service
@RequiredArgsConstructor

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RegistrationRequestFieldMatch validator;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto) {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new RegistrationException("Can`t register user");
        }
        Errors errors = new BeanPropertyBindingResult(requestDto, "userRegistrationRequestDto");
        validator.validate(requestDto, errors);
        if (errors.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder("Validation failed: ");
            for (ObjectError error : errors.getAllErrors()) {
                errorMessage.append(error.getDefaultMessage()).append("; ");
            }
            throw new RegistrationException(errorMessage.toString());
        }
        User user = userMapper.toModel(requestDto);
        user.setFirstname(requestDto.getFirstname());
        user.setLastname(requestDto.getLastname());
        user.setShippingaddress(requestDto.getShippingaddress());
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }
}
