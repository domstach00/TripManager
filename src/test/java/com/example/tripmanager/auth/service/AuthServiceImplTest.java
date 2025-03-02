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
import java.nio.CharBuffer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;
    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;

    @Test
    void testLogin_Success() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");

        when(request.isSecure()).thenReturn(true);

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        String dummyJwt = "dummyJwt";
        when(jwtService.generateToken(authentication)).thenReturn(dummyJwt);
        int expirationMs = 60000;
        when(jwtService.getJwtExpirationMs()).thenReturn(expirationMs);
        String cookieName = "jwtCookie";
        when(jwtService.getJwtCookieName()).thenReturn(cookieName);

        authService.login(loginRequest, request, response);

        ArgumentCaptor<Cookie> cookieCaptor = ArgumentCaptor.forClass(Cookie.class);
        verify(response).addCookie(cookieCaptor.capture());
        Cookie jwtCookie = cookieCaptor.getValue();
        assertEquals(cookieName, jwtCookie.getName());
        assertEquals(dummyJwt, jwtCookie.getValue());
        assertTrue(jwtCookie.isHttpOnly());
        assertEquals("/", jwtCookie.getPath());
        assertEquals((expirationMs / 1000), jwtCookie.getMaxAge());
        assertTrue(jwtCookie.getSecure());
    }

    @Test
    void testLogin_NullParameters() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");

        // If loginRequest is null
        assertThrows(InvalidRequestException.class, () -> authService.login(null, request, response));
        // If request is null
        assertThrows(InvalidRequestException.class, () -> authService.login(loginRequest, null, response));
        // If response is null
        assertThrows(InvalidRequestException.class, () -> authService.login(loginRequest, request, null));
    }

    @Test
    void testRegister_Success() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@example.com");
        signupRequest.setPassword("password");

        when(accountRepository.existsByEmail(signupRequest.getEmail())).thenReturn(false);

        Account dummyAccount = new Account();
        try (var accountMapperMock = mockStatic(AccountMapper.class)) {
            accountMapperMock.when(() -> AccountMapper.fromSignUp(signupRequest))
                    .thenReturn(dummyAccount);

            String encodedPassword = "encodedPassword";
            when(passwordEncoder.encode(CharBuffer.wrap(signupRequest.getPassword())))
                    .thenReturn(encodedPassword);

            when(accountRepository.save(dummyAccount)).thenReturn(dummyAccount);

            Account result = authService.register(signupRequest);
            assertEquals(encodedPassword, dummyAccount.getPassword());
            assertTrue(dummyAccount.getRoles().contains(Role.ROLE_USER));
            verify(accountRepository, times(1)).save(dummyAccount);
            accountMapperMock.verify(() -> AccountMapper.fromSignUp(signupRequest), times(1));
            assertEquals(dummyAccount, result);
        }
    }

    @Test
    void testRegister_NullParam() {
        assertThrows(IllegalArgumentException.class,
                () -> authService.register(null));
    }

    @Test
    void testRegister_AlreadyExists() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@example.com");
        signupRequest.setPassword("password");

        when(accountRepository.existsByEmail(signupRequest.getEmail())).thenReturn(true);

        AccountAlreadyExistsException exception = assertThrows(AccountAlreadyExistsException.class,
                () -> authService.register(signupRequest));
        assertEquals("Account with email test@example.com already exists", exception.getMessage());
        verify(accountRepository, times(1)).existsByEmail(signupRequest.getEmail());
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    void testLogoutUser() {
        when(request.isSecure()).thenReturn(true);
        String cookieName = "jwtCookie";
        when(jwtService.getJwtCookieName()).thenReturn(cookieName);

        authService.logoutUser(request, response);

        ArgumentCaptor<Cookie> cookieCaptor = ArgumentCaptor.forClass(Cookie.class);
        verify(response).addCookie(cookieCaptor.capture());
        Cookie cookie = cookieCaptor.getValue();
        assertEquals(cookieName, cookie.getName());
        assertEquals("", cookie.getValue());
        assertEquals(0, cookie.getMaxAge());
        assertTrue(cookie.isHttpOnly());
        assertEquals("/", cookie.getPath());
        assertTrue(cookie.getSecure());
    }
}
