package com.example.tripmanager.auth.controller;

import com.example.tripmanager.account.model.Account;
import com.example.tripmanager.account.model.AccountDto;
import com.example.tripmanager.auth.model.LoginRequest;
import com.example.tripmanager.auth.model.SignupRequest;
import com.example.tripmanager.auth.service.AuthService;
import com.example.tripmanager.shared.model.messageResponse.MessageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.security.Principal;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private com.example.tripmanager.account.service.AccountService accountService;

    @InjectMocks
    @Spy
    private AuthController authController;

    private Account dummyAccount;
    private AccountDto dummyAccountDto;
    private Principal dummyPrincipal;
    private HttpServletRequest dummyRequest;
    private HttpServletResponse dummyResponse;

    @BeforeEach
    void setUp() {
        dummyAccount = new Account();
        dummyAccount.setId("acc1");

        dummyAccountDto = new AccountDto();
        dummyAccountDto.setId("acc1");

        lenient().when(accountService.getCurrentAccount(any(Principal.class)))
                .thenReturn(dummyAccount);

        dummyPrincipal = () -> "user@example.com";

        dummyRequest = org.mockito.Mockito.mock(HttpServletRequest.class);
        dummyResponse = org.mockito.Mockito.mock(HttpServletResponse.class);
    }

    @Test
    void testLoginSuccess() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("user@example.com");
        loginRequest.setPassword("password");

        MessageResponse response = authController.login(loginRequest, dummyRequest, dummyResponse);

        verify(authService, times(1))
                .login(eq(loginRequest), eq(dummyRequest), eq(dummyResponse));

        assertNotNull(response);
        assertEquals("Login successful", response.getMessage());
    }

    @Test
    void testLogin_NullLoginRequestForm() {
        assertThrows(NullPointerException.class,
                () -> authController.login(null, dummyRequest, dummyResponse));
    }

    @Test
    void testLogin_ServiceThrowsException() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("user@example.com");
        loginRequest.setPassword("password");

        RuntimeException ex = new RuntimeException("Login error");
        doThrow(ex).when(authService)
                .login(eq(loginRequest), eq(dummyRequest), eq(dummyResponse));

        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> authController.login(loginRequest, dummyRequest, dummyResponse));

        assertEquals("Login error", thrown.getMessage());
    }

    @Test
    void testRegisterSuccess() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("user@example.com");
        signupRequest.setPassword("password");

        authController.register(signupRequest, dummyRequest);

        verify(authService, times(1)).register(eq(signupRequest));
    }

    @Test
    void testRegister_NullRequest() {
        assertThrows(NullPointerException.class,
                () -> authController.register(null, dummyRequest));
    }

    @Test
    void testRegister_ServiceThrowsException() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("user@example.com");
        signupRequest.setPassword("password");

        RuntimeException ex = new RuntimeException("Register error");
        doThrow(ex).when(authService).register(eq(signupRequest));

        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> authController.register(signupRequest, dummyRequest));

        assertEquals("Register error", thrown.getMessage());
    }

    @Test
    void testLogoutSuccess() {
        MessageResponse response = authController.logout(dummyPrincipal, dummyRequest, dummyResponse);

        verify(authService, times(1))
                .logoutUser(eq(dummyRequest), eq(dummyResponse));

        assertNotNull(response);
        assertEquals("Logout successful", response.getMessage());
    }

    @Test
    void testLogout_ServiceThrowsException() {
        RuntimeException ex = new RuntimeException("Logout error");
        doThrow(ex).when(authService)
                .logoutUser(eq(dummyRequest), eq(dummyResponse));

        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> authController.logout(dummyPrincipal, dummyRequest, dummyResponse));

        assertEquals("Logout error", thrown.getMessage());
    }

    @Test
    void testGetCurrentLoggedInAccountSuccess() {
        doReturn(dummyAccountDto).when(authController).toDto(dummyAccount);

        AccountDto result = authController.getCurrentLoggedInAccount(dummyPrincipal);

        verify(accountService, times(1)).getCurrentAccount(eq(dummyPrincipal));
        assertNotNull(result);
        assertEquals("acc1", result.getId());
    }

    @Test
    void testGetCurrentLoggedInAccount_NullPrincipal() {
        assertThrows(IllegalArgumentException.class,
                () -> authController.getCurrentLoggedInAccount(null));
    }
}
