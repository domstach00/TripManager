package com.example.tripmanager.auth.model;

import jakarta.validation.constraints.NotBlank;

public class SignupRequest {
    @NotBlank(message = "Email cannot be null")
    private String email;
    @NotBlank(message = "Name cannot be null")
    private String name;
    @NotBlank(message = "Password cannot be null")
    private String password;

    public SignupRequest() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
