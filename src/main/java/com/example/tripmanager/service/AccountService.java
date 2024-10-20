package com.example.tripmanager.service;

import com.example.tripmanager.model.account.Account;

import java.security.Principal;
import java.util.Optional;

public interface AccountService {
    Account getCurrentAccount(Principal principal);
    Optional<Account> getAccountById(String id);
    Optional<Account> getAccountByEmail(String email);
}
