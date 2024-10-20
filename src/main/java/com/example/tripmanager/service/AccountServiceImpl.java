package com.example.tripmanager.service;

import com.example.tripmanager.model.account.Account;
import com.example.tripmanager.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public Account getCurrentAccount(Principal principal) {
        if (principal == null) {
            return null;
        } else if (principal instanceof User user) {
            return accountRepository.findByUsername(user.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        } else {
            return accountRepository.findByEmail(principal.getName())
                    .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        }
    }

    @Override
    public Optional<Account> getAccountById(String id) {
        return accountRepository.findById(id);
    }

    @Override
    public Optional<Account> getAccountByEmail(String email) {
        return accountRepository.findByEmail(email);
    }


}
