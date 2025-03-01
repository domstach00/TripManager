package com.example.tripmanager.auth.service;

import com.example.tripmanager.account.exception.AccountAlreadyExistsException;
import com.example.tripmanager.shared.exception.InvalidRequestException;
import com.example.tripmanager.account.mapper.AccountMapper;
import com.example.tripmanager.auth.model.LoginRequest;
import com.example.tripmanager.auth.model.SignupRequest;
import com.example.tripmanager.account.model.Account;
import com.example.tripmanager.account.model.Role;
import com.example.tripmanager.account.repository.AccountRepository;
import com.example.tripmanager.auth.security.jwt.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public void login(LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {
        if (loginRequest == null || request == null || response == null) {
            throw new InvalidRequestException();
        }
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
    }

    @Override
    public Account register(SignupRequest signupRequest) {
        if (signupRequest == null) {
            throw new IllegalArgumentException("SignupRequest form cannot be null");
        }
        if (accountRepository.existsByEmail(signupRequest.getEmail())) {
            throw new AccountAlreadyExistsException("Account with email %s already exists".formatted(signupRequest.getEmail()));
        }
        // TODO add email service

        Account newAccount = AccountMapper.fromSignUp(signupRequest);
        setUserPassword(newAccount, signupRequest.getPassword());
        newAccount.setRoles(Set.of(Role.ROLE_USER));

        return accountRepository.save(newAccount);
    }

    public void logoutUser(HttpServletRequest request,
                           HttpServletResponse response) {
        Cookie jwtCookieToDelete = createJwtCookie("", request.isSecure(), 0);
        response.addCookie(jwtCookieToDelete);
    }

    private Cookie createJwtCookie(String token, boolean isRequestSecure, int maxCookieAgeInSec) {
        String cookieName = this.jwtService.getJwtCookieName();
        Cookie cookie = new Cookie(cookieName, token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(maxCookieAgeInSec);
        cookie.setSecure(isRequestSecure);
        return cookie;
    }

    private void setUserPassword(Account account, String password) {
        account.setPassword(passwordEncoder.encode(CharBuffer.wrap(password)));
    }
}
