package com.example.tripmanager.service;

import com.example.tripmanager.model.account.Account;
import com.example.tripmanager.model.account.AccountDto;
import com.example.tripmanager.model.account.Role;

import java.security.Principal;
import java.util.Optional;
import java.util.Set;

public interface AccountService {
    Account getCurrentAccount();
    Account getAccountFromPrincipal(Principal principal);
    AccountDto getCurrentAccountDto();
    AccountDto getAccountDtoFromPrincipal(Principal principal);
    Set<Role> getCurrentAccountRoles();
    Optional<Account> getAccountById(String id);
    Optional<Account> getAccountByEmail(String email);
}
