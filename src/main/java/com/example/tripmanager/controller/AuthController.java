package com.example.tripmanager.controller;

import com.example.tripmanager.mapper.UserMapper;
import com.example.tripmanager.model.*;
import com.example.tripmanager.model.user.User;
import com.example.tripmanager.model.user.UserDto;
import com.example.tripmanager.service.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(AuthController.authControllerUrl)
public class AuthController {
    protected final static String authControllerUrl = "/api/auth";
    private final static String loginPostUrl = "/login";
    private final static String registerPostUrl = "/register";

    @Autowired
    private UserAuthService userAuthService;
    @Autowired
    private UserMapper userMapper;

    @PostMapping(path = loginPostUrl)
    public ResponseEntity<UserDto> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(userAuthService.login(loginRequest));
    }

    @PostMapping(registerPostUrl)
    public ResponseEntity<UserDto> register(@RequestBody SignupRequest signupRequest) {
        User user = userAuthService.register(signupRequest);
        return ResponseEntity.ok(userMapper.toUserDto(user));
    }

    public static String getLoginPostUrl() {
        return authControllerUrl + loginPostUrl;
    }

    public static String getRegisterPostUrl() {
        return authControllerUrl + registerPostUrl;
    }
}
