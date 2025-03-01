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
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
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
        log.info("Attempting to login with email={} from ip={}", loginRequest.getEmail(), request.getRemoteAddr());
        authService.login(loginRequest, request, response);
        return new MessageResponse("Login successful");
    }

    @PostMapping(registerPostUrl)
    @ResponseStatus(HttpStatus.CREATED)
    public AccountDto register(@Valid @RequestBody SignupRequest signupRequest, HttpServletRequest request) {
        log.info("Attempting to register with email={} from ip={}", signupRequest.getEmail(), request.getRemoteAddr());
        Account account = authService.register(signupRequest);
        return toDto(account);
    }

    @GetMapping(logoutGetUrl)
    public MessageResponse logout(Principal principal,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {
        Account currentAccount = getCurrentAccount(principal);
        log.info("Attempting to logout user={} from ip={}", currentAccount.getId(), request.getPathInfo());
        this.authService.logoutUser(request, response);
        return new MessageResponse("Logout successful");
    }

    @GetMapping("/currentaccount")
    public AccountDto getCurrentLoggedInAccount(
            Principal principal
    ) {
        Account currentAccount = getCurrentAccount(principal);
        return toDto(currentAccount);
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
