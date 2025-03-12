package com.example.tripmanager.account.service;

import com.example.tripmanager.account.model.Account;
import com.example.tripmanager.account.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
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

    @Override
    public Page<Account> getAccountsByIds(Pageable pageable, List<String> accountIds) {
        if (pageable == null || accountIds == null) {
            log.warn("Invalid input: pageable or accountIds is null");
            throw new IllegalArgumentException("Pageable and accountIds must not be null");
        }

        if (accountIds.isEmpty()) {
            log.warn("No account IDs provided, returning an empty page.");
            return Page.empty(pageable);
        }

        Page<Account> foundAccounts = accountRepository.getAccountsByIdIn(pageable, accountIds);

        if (foundAccounts.isEmpty()) {
            log.warn("No accounts found for the given IDs: {}", String.join(",", accountIds));
        } else if (accountIds.size() != (int) foundAccounts.getTotalElements()) {
            long missingCount = accountIds.size() - foundAccounts.getTotalElements();
            log.warn("Some accounts were not found. Missing count: {}", missingCount);
        }

        return foundAccounts;
    }
}
