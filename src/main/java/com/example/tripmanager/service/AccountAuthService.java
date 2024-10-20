package com.example.tripmanager.service;

import com.example.tripmanager.model.auth.LoginRequest;
import com.example.tripmanager.model.auth.SignupRequest;
import com.example.tripmanager.model.account.Account;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AccountAuthService {
    void login(LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response);
    Account register(SignupRequest signupRequest);
    void logoutUser(HttpServletRequest request, HttpServletResponse response);
}
