package com.example.tripmanager.mapper;

import com.example.tripmanager.model.account.Account;
import com.example.tripmanager.model.account.AccountDto;
import com.example.tripmanager.model.auth.SignupRequest;

public class AccountMapper {
    public static AccountDto toDto(Account account) {
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
        account.setUsername(signupRequest.getUsername());
        return account;
    }
}
