package com.example.tripmanager.service;

import com.example.tripmanager.exception.AccountAlreadyExistsException;
import com.example.tripmanager.exception.WrongCredentialsException;
import com.example.tripmanager.model.LoginRequest;
import com.example.tripmanager.model.SignupRequest;
import com.example.tripmanager.model.account.Account;
import com.example.tripmanager.model.account.AccountDto;
import com.example.tripmanager.model.account.Role;
import com.example.tripmanager.repository.AccountRepository;
import com.example.tripmanager.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class AccountAuthServiceImpl implements AccountAuthService {
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

    private void setUserPassword(Account account, String password) {
        account.setPassword(passwordEncoder.encode(CharBuffer.wrap(password)));
    }
}
