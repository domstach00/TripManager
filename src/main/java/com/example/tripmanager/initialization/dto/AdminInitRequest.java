package com.example.tripmanager.initialization.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AdminInitRequest {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(max = 20)
    private String username;

    @NotBlank
    @Size(min = 8, max = 120)
    private String password;

    private boolean sendActivationEmail;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isSendActivationEmail() {
        return sendActivationEmail;
    }

    public void setSendActivationEmail(boolean sendActivationEmail) {
        this.sendActivationEmail = sendActivationEmail;
    }
}
