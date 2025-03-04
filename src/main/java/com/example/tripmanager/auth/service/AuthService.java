package com.example.tripmanager.auth.service;

import com.example.tripmanager.auth.model.LoginRequest;
import com.example.tripmanager.auth.model.SignupRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    void login(LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response);
    void register(SignupRequest signupRequest);
    void logoutUser(HttpServletRequest request, HttpServletResponse response);
    boolean activateAccount(String tokenValue);
}
