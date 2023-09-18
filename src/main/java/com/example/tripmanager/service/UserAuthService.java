package com.example.tripmanager.service;

import com.example.tripmanager.model.LoginRequest;
import com.example.tripmanager.model.SignupRequest;
import com.example.tripmanager.model.user.User;
import com.example.tripmanager.model.user.UserDto;

public interface UserAuthService {
    UserDto login(LoginRequest loginRequest);
    User register(SignupRequest signupRequest);
}
