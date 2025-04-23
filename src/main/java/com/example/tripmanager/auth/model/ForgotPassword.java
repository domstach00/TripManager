package com.example.tripmanager.auth.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ForgotPassword(@Email(message = "Given text is not an email")
                             @NotBlank(message = "Email cannot be blank")
                             String email) {
}
