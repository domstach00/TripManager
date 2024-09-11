package com.example.tripmanager.controller;

import com.example.tripmanager.model.*;
import com.example.tripmanager.model.account.Account;
import com.example.tripmanager.model.account.AccountDto;
import com.example.tripmanager.service.AccountAuthService;
import com.example.tripmanager.service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    private AccountAuthService accountAuthService;
    @Autowired
    private AccountService accountService;

    @PostMapping(path = loginPostUrl)
    public ResponseEntity<AccountDto> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(accountAuthService.login(loginRequest));
    }

    @PostMapping(registerPostUrl)
    public ResponseEntity<AccountDto> register(@RequestBody SignupRequest signupRequest) {
        Account account = accountAuthService.register(signupRequest);
        return ResponseEntity.ok(Account.toDto(account));
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response) {
        return accountAuthService.logoutUser(request, response);
    }

    @GetMapping("/currentaccount")
    public AccountDto getCurrentAccount() {
        return Account.toDto(accountService.getCurrentAccount());
    }

    public static String getLoginPostUrl() {
        return authControllerUrl + loginPostUrl;
    }

    public static String getRegisterPostUrl() {
        return authControllerUrl + registerPostUrl;
    }
}
