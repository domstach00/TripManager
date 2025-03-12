package com.example.tripmanager.account.service;

import com.example.tripmanager.account.model.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

public interface AccountService {
    Account getCurrentAccount(Principal principal);
    Optional<Account> getAccountById(String id);
    Optional<Account> getAccountByEmail(String email);
    Page<Account> getAccountsByIds(Pageable pageable, List<String> ids);
}
