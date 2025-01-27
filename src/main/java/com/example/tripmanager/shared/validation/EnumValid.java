package com.example.tripmanager.shared.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EnumValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumValid {
    Class<? extends Enum<?>> enumClass();
    String message() default "Invalid value. Must be any of enum values";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
