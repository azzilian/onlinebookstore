package com.onlinebookstore.onlinebookstore.service.impl.validator;

import com.onlinebookstore.onlinebookstore.exeption.ValidationException;
import com.onlinebookstore.onlinebookstore.service.interfaces.FieldMatch;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.util.Objects;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {
    private String firstFieldName;
    private String secondFieldName;

    @Override
    public void initialize(final FieldMatch constraintAnnotation) {
        firstFieldName = constraintAnnotation.field();
        secondFieldName = constraintAnnotation.fieldMatch();
    }

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
        try {
            Field firstField = value.getClass().getDeclaredField(firstFieldName);
            Field secondField = value.getClass().getDeclaredField(secondFieldName);
            firstField.setAccessible(true);
            secondField.setAccessible(true);

            Object fieldValue = firstField.get(value);
            Object fieldMatchValue = secondField.get(value);

            return Objects.equals(fieldValue, fieldMatchValue);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new ValidationException("Error validating fields", e);
        }
    }
}
