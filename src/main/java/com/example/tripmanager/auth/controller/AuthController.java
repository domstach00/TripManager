package com.example.tripmanager.auth.controller;

import com.example.tripmanager.account.mapper.AccountMapper;
import com.example.tripmanager.account.model.Account;
import com.example.tripmanager.account.model.AccountDto;
import com.example.tripmanager.auth.model.LoginRequest;
import com.example.tripmanager.auth.model.SignupRequest;
import com.example.tripmanager.shared.controller.AbstractController;
import com.example.tripmanager.shared.model.messageResponse.MessageResponse;
import com.example.tripmanager.auth.service.AuthService;
import com.example.tripmanager.shared.token.exception.TokenValidationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping(AuthController.AUTH_CONTROLLER_URL)
public class AuthController extends AbstractController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    protected final static String AUTH_CONTROLLER_URL = "/api/auth";
    private final static String LOGIN_URL = "/login";
    private final static String REGISTER_URL = "/register";
    private static final String ACTIVATE_ACCOUNT_URL = "/activate-account";
    public static final String LOGOUT_URL = "/logout";

    @Autowired
    private AuthService authService;

    protected AccountDto toDto(Account account) {
        return AccountMapper.toDto(account);
    }

    @PostMapping(path = LOGIN_URL)
    public MessageResponse login(@RequestBody LoginRequest loginRequest,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        log.info("Login attempt for email={} from IP={}", loginRequest.getEmail(), request.getRemoteAddr());
        authService.login(loginRequest, request, response);
        return new MessageResponse("Login successful");
    }

    @PostMapping(REGISTER_URL)
    @ResponseStatus(HttpStatus.CREATED)
    public MessageResponse register(@Valid @RequestBody SignupRequest signupRequest, HttpServletRequest request) {
        log.info("Registration attempt for email={} from IP={}", signupRequest.getEmail(), request.getRemoteAddr());
        authService.register(signupRequest);
        return new MessageResponse("Registration successful. Please check your email for activation instructions");
    }

    @GetMapping(LOGOUT_URL)
    public MessageResponse logout(Principal principal,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {
        Account currentAccount = getCurrentAccount(principal);
        log.info("Logout request for user ID={} from IP={}", currentAccount.getId(), request.getRemoteAddr());
        this.authService.logoutUser(request, response);
        return new MessageResponse("Logout successful");
    }

    @GetMapping(ACTIVATE_ACCOUNT_URL)
    public MessageResponse activateAccount(
            @RequestParam(name = "token")
            @NotBlank
            @Size(max = 256)
            String token
    ) {
        log.debug("Account activation attempt with token={}", token);
        boolean activationSuccess = authService.activateAccount(token);
        if (!activationSuccess) {
            log.debug("Activation token is invalid or expired, token={}", token);
            throw new TokenValidationException("Activation token is invalid or expired");
        }
        return new MessageResponse("Account activated successfully");
    }

    @GetMapping("/currentaccount")
    public AccountDto getCurrentLoggedInAccount(
            Principal principal
    ) {
        Account currentAccount = getCurrentAccount(principal);
        return toDto(currentAccount);
    }

    public static String getLoginUrl() {
        return AUTH_CONTROLLER_URL + LOGIN_URL;
    }

    public static String getRegisterUrl() {
        return AUTH_CONTROLLER_URL + REGISTER_URL;
    }

    public static String getActivateAccountUrl() {
        return AUTH_CONTROLLER_URL + ACTIVATE_ACCOUNT_URL;
    }

    public static String getLogoutUrl() {
        return AUTH_CONTROLLER_URL + LOGOUT_URL;
    }
}
