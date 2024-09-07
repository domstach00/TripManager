package com.example.tripmanager.service;

import com.example.tripmanager.model.LoginRequest;
import com.example.tripmanager.model.SignupRequest;
import com.example.tripmanager.model.account.Account;
import com.example.tripmanager.model.account.AccountDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AccountAuthService {
    AccountDto login(LoginRequest loginRequest);
    Account register(SignupRequest signupRequest);
    String logoutUser(HttpServletRequest request, HttpServletResponse response);
}
