package com.example.tripmanager.controller;

import com.example.tripmanager.mapper.AccountMapper;
import com.example.tripmanager.model.account.Account;
import com.example.tripmanager.model.account.AccountDto;
import com.example.tripmanager.model.auth.LoginRequest;
import com.example.tripmanager.model.auth.SignupRequest;
import com.example.tripmanager.model.messageResponse.MessageResponse;
import com.example.tripmanager.service.AccountAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@RestController
@RequestMapping(AuthController.authControllerUrl)
public class AuthController extends AbstractController {
    protected final static String authControllerUrl = "/api/auth";
    private final static String loginPostUrl = "/login";
    private final static String registerPostUrl = "/register";

    @Autowired
    private AccountAuthService accountAuthService;

    protected AccountDto toDto(Account account) {
        return AccountMapper.toDto(account);
    }

    @PostMapping(path = loginPostUrl)
    public MessageResponse login(@RequestBody LoginRequest loginRequest,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        accountAuthService.login(loginRequest, request, response);
        return new MessageResponse("Login successful");
    }

    @PostMapping(registerPostUrl)
    public ResponseEntity<AccountDto> register(@RequestBody SignupRequest signupRequest) {
        Account account = accountAuthService.register(signupRequest);
        return ResponseEntity.ok(toDto(account));
    }

    @GetMapping("/logout")
    public MessageResponse logout(HttpServletRequest request,
                                  HttpServletResponse response) {
        this.accountAuthService.logoutUser(request, response);
        return new MessageResponse("Logout successful");
    }

    @GetMapping("/currentaccount")
    public AccountDto getCurrentLoggedInAccount(
            Principal principal
    ) {
        return toDto(getCurrentAccount(principal));
    }

    public static String getLoginPostUrl() {
        return authControllerUrl + loginPostUrl;
    }

    public static String getRegisterPostUrl() {
        return authControllerUrl + registerPostUrl;
    }
}
