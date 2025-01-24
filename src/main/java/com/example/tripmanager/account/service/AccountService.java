package com.example.tripmanager.account.service;

import com.example.tripmanager.account.model.Account;

import java.security.Principal;
import java.util.Optional;

public interface AccountService {
    Account getCurrentAccount(Principal principal);
    Optional<Account> getAccountById(String id);
    Optional<Account> getAccountByEmail(String email);
}
