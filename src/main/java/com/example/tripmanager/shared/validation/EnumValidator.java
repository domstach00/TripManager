package com.example.tripmanager.shared.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.stream.Collectors;

public class EnumValidator implements ConstraintValidator<EnumValid, String> {
    private String allowedValues;
    private Enum<?>[] enumConstants;

    @Override
    public void initialize(EnumValid constraintAnnotation) {
        this.enumConstants = constraintAnnotation.enumClass().getEnumConstants();
        this.allowedValues = Arrays.stream(enumConstants)
                .map(Enum::name)
                .collect(Collectors.joining(", "));

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        boolean isValid = Arrays.stream(enumConstants)
                .map(Enum::name)
                .anyMatch(value::equals);

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Invalid value. Must be one of: " + allowedValues)
                    .addConstraintViolation();
        }
        return isValid;
    }
}
