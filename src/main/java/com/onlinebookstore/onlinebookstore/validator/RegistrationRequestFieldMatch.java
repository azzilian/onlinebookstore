package com.onlinebookstore.onlinebookstore.validator;

import com.onlinebookstore.onlinebookstore.dto.user.UserRegistrationRequestDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class RegistrationRequestFieldMatch implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return UserRegistrationRequestDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserRegistrationRequestDto requestDto = (UserRegistrationRequestDto) target;
        if (!requestDto.getPassword().equals(requestDto.getRepeatpassword())) {
            errors.rejectValue("repeatpassword", "password.mismatch", "Passwords do not match");
        }
    }
}
