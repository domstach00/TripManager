package com.example.tripmanager.account.service;

import com.example.tripmanager.account.model.Account;
import com.example.tripmanager.account.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {
    private static final Logger log = LoggerFactory.getLogger(AccountServiceImpl.class);
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public Account getCurrentAccount(Principal principal) {
        if (principal == null) {
            log.debug("Attempt to retrieve current account failed: principal is null");
            return null;
        } else if (principal instanceof User user) {
            log.debug("Retrieving account for username: {}", user.getUsername());
            return accountRepository.findByUsername(user.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        } else {
            log.debug("Retrieving account for email: {}", principal.getName());
            return accountRepository.findByEmail(principal.getName())
                    .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        }
    }

    @Override
    public Optional<Account> getAccountById(String id) {
        log.debug("Fetching account by ID: {}", id);
        Optional<Account> account = accountRepository.findById(id);

        if (account.isEmpty()) {
            log.warn("No account found for ID: {}", id);
        }
        return account;
    }

    @Override
    public Optional<Account> getAccountByEmail(String email) {
        log.debug("Fetching account by email: {}", email);
        Optional<Account> account = accountRepository.findByEmail(email);

        if (account.isEmpty()) {
            log.warn("No account found for email: {}", email);
        }
        return account;
    }
}
