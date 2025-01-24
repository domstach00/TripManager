package com.example.tripmanager.account.mapper;

import com.example.tripmanager.account.model.Account;
import com.example.tripmanager.account.model.AccountDto;
import com.example.tripmanager.auth.model.SignupRequest;

import java.util.Objects;

public class AccountMapper {
    public static AccountDto toDto(Account account) {
        if (Objects.isNull(account)) {
            return null;
        }
        AccountDto accountDto = new AccountDto();
        accountDto.setId(account.getId());
        accountDto.setEmail(account.getEmail());
        accountDto.setRoles(account.getRoles());
        accountDto.setUsername(account.getUsername());
        return accountDto;
    }

    public static Account fromSignUp(SignupRequest signupRequest) {
        Account account = new Account();
        account.setEmail(signupRequest.getEmail());
        account.setName(signupRequest.getName());
        return account;
    }
}
