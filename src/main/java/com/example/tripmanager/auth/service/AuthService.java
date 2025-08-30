package com.example.tripmanager.auth.service;

import com.example.tripmanager.auth.model.ForgotPassword;
import com.example.tripmanager.auth.model.LoginRequest;
import com.example.tripmanager.auth.model.SignupRequest;
import com.example.tripmanager.initialization.dto.AdminInitRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    void login(LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response);
    void register(SignupRequest signupRequest);
    void registerAdmin(AdminInitRequest adminInitRequest,  boolean activateAccountOnDefault);
    void logoutUser(HttpServletRequest request, HttpServletResponse response);
    boolean activateAccount(String tokenValue);
    void forgotPasswordProcess(ForgotPassword forgotPassword);
}
