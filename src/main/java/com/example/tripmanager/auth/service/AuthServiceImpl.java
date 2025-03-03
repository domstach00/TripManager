package com.example.tripmanager.auth.service;

import com.example.tripmanager.account.exception.AccountAlreadyExistsException;
import com.example.tripmanager.email.service.EmailService;
import com.example.tripmanager.shared.exception.InvalidRequestException;
import com.example.tripmanager.account.mapper.AccountMapper;
import com.example.tripmanager.auth.model.LoginRequest;
import com.example.tripmanager.auth.model.SignupRequest;
import com.example.tripmanager.account.model.Account;
import com.example.tripmanager.account.model.Role;
import com.example.tripmanager.account.repository.AccountRepository;
import com.example.tripmanager.auth.security.jwt.JwtService;
import com.example.tripmanager.shared.token.model.Token;
import com.example.tripmanager.shared.token.model.TokenType;
import com.example.tripmanager.shared.token.service.TokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {
    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private EmailService emailService;
    @Autowired
    private TokenService tokenService;

    @Override
    public void login(LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {
        if (loginRequest == null || request == null || response == null) {
            log.error("Login request failed: loginRequest, request, or response is null");
            throw new InvalidRequestException();
        }
        log.info("Login attempt for user: {}", loginRequest.getEmail());
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        );
        Authentication authentication = authenticationManager.authenticate(
                usernamePasswordAuthenticationToken
        );

        String jwt = jwtService.generateToken(authentication);
        int maxCookieAgeInSec = jwtService.getJwtExpirationMs() / 1000;
        Cookie cookie = createJwtCookie(jwt, request.isSecure(), maxCookieAgeInSec);
        response.addCookie(cookie);
        log.info("User {} successfully logged in with ip={}", loginRequest.getEmail(), request.getRemoteAddr());
    }

    @Override
    public Account register(SignupRequest signupRequest) {
        if (signupRequest == null) {
            log.error("Registration failed: SignupRequest form is null");
            throw new IllegalArgumentException("SignupRequest form cannot be null");
        }
        log.info("Registration attempt for email: {}", signupRequest.getEmail());
        if (accountRepository.existsByEmail(signupRequest.getEmail())) {
            log.warn("Registration failed: Account with email '{}' already exists", signupRequest.getEmail());
            throw new AccountAlreadyExistsException("Account with email %s already exists".formatted(signupRequest.getEmail()));
        }

        Account newAccount = AccountMapper.fromSignUp(signupRequest);
        setUserPassword(newAccount, signupRequest.getPassword());
        newAccount.setRoles(Set.of(Role.ROLE_USER));

        Account createdAccount = accountRepository.save(newAccount);
        Token generatedToken = tokenService.generateToken(createdAccount.getId(), TokenType.ACCOUNT_ACTIVATION);
        emailService.sendWelcomeEmail(createdAccount, generatedToken.getTokenValue());
        log.info("User registered successfully with email: {}", createdAccount.getEmail());
        return createdAccount;
    }

    public void logoutUser(HttpServletRequest request,
                           HttpServletResponse response) {
        Cookie jwtCookieToDelete = createJwtCookie("", request.isSecure(), 0);
        response.addCookie(jwtCookieToDelete);
        log.info("User successfully logged out");
    }

    private Cookie createJwtCookie(String token, boolean isRequestSecure, int maxCookieAgeInSec) {
        String cookieName = this.jwtService.getJwtCookieName();
        log.debug("Creating JWT cookie with name '{}' and maxAge '{}'", cookieName, maxCookieAgeInSec);
        Cookie cookie = new Cookie(cookieName, token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(maxCookieAgeInSec);
        cookie.setSecure(isRequestSecure);
        return cookie;
    }

    private void setUserPassword(Account account, String password) {
        log.debug("Encoding password for account with email: {}", account.getEmail());
        account.setPassword(passwordEncoder.encode(CharBuffer.wrap(password)));
    }
}
