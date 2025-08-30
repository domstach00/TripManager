package com.example.tripmanager.auth.service;

import com.example.tripmanager.account.exception.AccountAlreadyExistsException;
import com.example.tripmanager.auth.model.ForgotPassword;
import com.example.tripmanager.email.service.EmailService;
import com.example.tripmanager.initialization.dto.AdminInitRequest;
import com.example.tripmanager.shared.exception.InvalidRequestException;
import com.example.tripmanager.account.mapper.AccountMapper;
import com.example.tripmanager.auth.model.LoginRequest;
import com.example.tripmanager.auth.model.SignupRequest;
import com.example.tripmanager.account.model.Account;
import com.example.tripmanager.account.model.Role;
import com.example.tripmanager.account.repository.AccountRepository;
import com.example.tripmanager.auth.security.jwt.JwtService;
import com.example.tripmanager.shared.token.model.token.AccountActivationToken;
import com.example.tripmanager.shared.token.model.token.PasswordResetToken;
import com.example.tripmanager.shared.token.model.token.Token;
import com.example.tripmanager.shared.token.model.TokenType;
import com.example.tripmanager.shared.token.service.TokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.CharBuffer;
import java.util.Map;
import java.util.Optional;
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
            log.warn("Login request failed: loginRequest, request, or response is null");
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
    @Transactional
    public void register(SignupRequest signupRequest) {
        if (signupRequest == null) {
            log.warn("Registration failed: SignupRequest form is null");
            throw new IllegalArgumentException("SignupRequest form cannot be null");
        }
        log.info("Registration attempt for email: {}", signupRequest.getEmail());
        if (accountRepository.existsByEmail(signupRequest.getEmail())) {
            log.warn("Registration failed: Account with email '{}' already exists", signupRequest.getEmail());
            throw new AccountAlreadyExistsException("Account with email %s already exists".formatted(signupRequest.getEmail()));
        }

        Account newAccount = AccountMapper.fromSignUp(signupRequest);
        newAccount.setPassword(encodePassword(signupRequest.getPassword()));
        newAccount.setRoles(Set.of(Role.ROLE_USER));

        Account createdAccount = accountRepository.save(newAccount);
        Token generatedToken = tokenService.generateToken(createdAccount.getId(), TokenType.ACCOUNT_ACTIVATION);
        emailService.sendWelcomeEmail(createdAccount, generatedToken.getTokenValue());
        log.info("User registered successfully with email: {}", createdAccount.getEmail());
    }

    @Override
    @Transactional
    public void registerAdmin(AdminInitRequest adminInitRequest, boolean activateAccountOnDefault) {
        if (adminInitRequest == null) {
            log.warn("Registration failed: AdminInitRequest form is null");
            throw new IllegalArgumentException("AdminInitRequest form cannot be null");
        }
        log.info("Admin registration attempt for email: {}", adminInitRequest.getEmail());
        if (accountRepository.existsByEmail(adminInitRequest.getEmail())) {
            log.warn("Registration failed: Account with email '{}' already exists", adminInitRequest.getEmail());
            throw new AccountAlreadyExistsException("Account with email %s already exists".formatted(adminInitRequest.getEmail()));
        }

        Account newAccountAdmin = AccountMapper.fromSignUpAdmin(adminInitRequest);
        newAccountAdmin.setPassword(encodePassword(adminInitRequest.getPassword()));
        newAccountAdmin.setRoles(Set.of(Role.ROLE_ADMIN));
        newAccountAdmin.setEnabled(activateAccountOnDefault || !adminInitRequest.isSendActivationEmail());

        Account createdAccount = accountRepository.save(newAccountAdmin);
        if (adminInitRequest.isSendActivationEmail()) {
            Token token = tokenService.generateToken(createdAccount.getId(), TokenType.ACCOUNT_ACTIVATION);
            emailService.sendWelcomeEmail(createdAccount, token.getTokenValue());
        }
        log.info("Admin registered successfully with email: {}", createdAccount.getEmail());
    }

    public void logoutUser(HttpServletRequest request,
                           HttpServletResponse response) {
        Cookie jwtCookieToDelete = createJwtCookie("", request.isSecure(), 0);
        response.addCookie(jwtCookieToDelete);
        log.info("User successfully logged out");
    }

    @Override
    @Transactional
    public boolean activateAccount(String tokenValue) {
        if (StringUtils.isBlank(tokenValue)) {
            log.warn("Token value for account activation is blank, token={}", tokenValue);
            return false;
        }
        final TokenType expectedTokenType = TokenType.ACCOUNT_ACTIVATION;
        Optional<AccountActivationToken> tokenOpt = tokenService.getToken(tokenValue, expectedTokenType);
        if (tokenOpt.isEmpty()) {
            return false;
        }

        Optional<Account> accountToActivateOpt = accountRepository.findById(tokenOpt.get().getAccountId());
        if (accountToActivateOpt.isEmpty()) {
            log.warn("Account with Id {} to activate is not present in database or is deleted", tokenOpt.get().getAccountId());
            return false;
        }
        Account accountToActivate = accountToActivateOpt.get();
        accountToActivate.setEnabled(true);
        accountRepository.save(accountToActivate);
        log.info("Account with Id {} has been activated properly", accountToActivate.getId());
        return true;
    }

    @Override
    public void forgotPasswordProcess(ForgotPassword forgotPassword) {
        if (StringUtils.isBlank(forgotPassword.email())) {
            log.warn("Forgot password failed: Given email is blank");
            return;
        }

        final Optional<Account> accountOpt = accountRepository.findByEmail(forgotPassword.email());
        if (accountOpt.isEmpty()) {
            log.warn("Forgot password failed: Account does not exists");
            return;
        }
        Account account = accountOpt.get();

        Optional<PasswordResetToken> activeForgotPasswordTokenOpt = tokenService.getPresentValidToken(account.getId(), TokenType.PASSWORD_RESET);
        PasswordResetToken token = activeForgotPasswordTokenOpt.orElseGet(() ->
                tokenService.generateToken(account.getId(), TokenType.PASSWORD_RESET, Map.of()));

        emailService.sendPasswordResetEmail(account, token.getTokenValue());
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

    private String encodePassword(String password) {
        return passwordEncoder.encode(CharBuffer.wrap(password));
    }
}
