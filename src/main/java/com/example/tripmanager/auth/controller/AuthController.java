package com.example.tripmanager.auth.controller;

import com.example.tripmanager.account.mapper.AccountMapper;
import com.example.tripmanager.account.model.Account;
import com.example.tripmanager.account.model.AccountDto;
import com.example.tripmanager.auth.model.LoginRequest;
import com.example.tripmanager.auth.model.SignupRequest;
import com.example.tripmanager.shared.controller.AbstractController;
import com.example.tripmanager.shared.model.messageResponse.MessageResponse;
import com.example.tripmanager.auth.service.AuthService;
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
    public static final String logoutGetUrl = "/logout";

    @Autowired
    private AuthService authService;

    protected AccountDto toDto(Account account) {
        return AccountMapper.toDto(account);
    }

    @PostMapping(path = loginPostUrl)
    public MessageResponse login(@RequestBody LoginRequest loginRequest,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        authService.login(loginRequest, request, response);
        return new MessageResponse("Login successful");
    }

    @PostMapping(registerPostUrl)
    public ResponseEntity<AccountDto> register(@RequestBody SignupRequest signupRequest) {
        Account account = authService.register(signupRequest);
        return ResponseEntity.ok(toDto(account));
    }

    @GetMapping(logoutGetUrl)
    public MessageResponse logout(HttpServletRequest request,
                                  HttpServletResponse response) {
        this.authService.logoutUser(request, response);
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

    public static String getLogoutGetUrl() {
        return authControllerUrl + logoutGetUrl;
    }
}
