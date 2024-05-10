package com.onlinebookstore.onlinebookstore.service.impl.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = FieldMatchValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldMatch {

    String message() default "Fields values don't match!";

    String field();

    String fieldMatch();

    @Target({ ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        FieldMatch[] value();
    }
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
