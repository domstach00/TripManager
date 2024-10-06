package com.example.tripmanager.service;

import com.example.tripmanager.exception.AccountAlreadyExistsException;
import com.example.tripmanager.exception.WrongCredentialsException;
import com.example.tripmanager.model.auth.LoginRequest;
import com.example.tripmanager.model.auth.SignupRequest;
import com.example.tripmanager.model.account.Account;
import com.example.tripmanager.model.account.AccountDto;
import com.example.tripmanager.model.account.Role;
import com.example.tripmanager.repository.AccountRepository;
import com.example.tripmanager.security.jwt.JwtService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.SessionCookieConfig;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;
import java.util.Optional;
import java.util.Set;

@Service
public class AccountAuthServiceImpl implements AccountAuthService {
    private final static String DEFAULT_SESSION_COOKIE_NAME = "auth-token";

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Override
    public AccountDto login(LoginRequest loginRequest) {
        Optional<Account> user = accountRepository.findByEmail(loginRequest.getEmail());

        if ( user.isEmpty() || !passwordEncoder.matches(CharBuffer.wrap(loginRequest.getPassword()), user.get().getPassword())) {
            throw new WrongCredentialsException("Wrong credentials");
        }

        AccountDto accountDto = Account.toDto(user.get());
        accountDto.setToken(jwtService.createToken(accountDto));
        return accountDto;
    }

    @Override
    public Account register(SignupRequest signupRequest) {
        if (accountRepository.existsByEmail(signupRequest.getEmail())) {
            throw new AccountAlreadyExistsException("Account with email %s already exists".formatted(signupRequest.getEmail()));
        }

        Account newAccount = Account.fromSignUp(signupRequest);
        setUserPassword(newAccount, signupRequest.getPassword());
        newAccount.setRoles(Set.of(Role.ROLE_USER));

        return accountRepository.save(newAccount);
    }

    public String logoutUser(HttpServletRequest request,
                           HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // Get the name of the session cookie
        String sessionCookieName = DEFAULT_SESSION_COOKIE_NAME;
        ServletContext context = request.getServletContext();
        if (context != null) {
            SessionCookieConfig config = context.getSessionCookieConfig();
            if (config != null && config.getName() != null) {
                sessionCookieName = config.getName();
            }
        }

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (sessionCookieName.equals(cookie.getName())) {
                    Cookie removeCookie = new Cookie(cookie.getName(), "");
                    removeCookie.setHttpOnly(true);
                    removeCookie.setMaxAge(0);
                    removeCookie.setPath("/");
                    if (request.isSecure()) {
                        removeCookie.setSecure(true);
                    }
                    response.addCookie(removeCookie);
                }
            }
        }

        SecurityContextLogoutHandler handler = new SecurityContextLogoutHandler();
        handler.logout(request, response, null);

        return "Logout successful";
    }

    private void setUserPassword(Account account, String password) {
        account.setPassword(passwordEncoder.encode(CharBuffer.wrap(password)));
    }
}
