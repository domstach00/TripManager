package com.example.tripmanager.service;

import com.example.tripmanager.exception.WrongCredentialsException;
import com.example.tripmanager.model.account.Account;
import com.example.tripmanager.model.account.AccountDto;
import com.example.tripmanager.model.account.Role;
import com.example.tripmanager.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Set;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public Account getCurrentAccount() {
        AccountDto accountDto = (AccountDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return accountRepository.findByUsername(accountDto.getUsername()).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }

    @Override
    public Account getAccountFromPrincipal(Principal principal) {
        if (principal == null) {
            throw new WrongCredentialsException("Principal not exists");
        }
        AccountDto accountDto = (AccountDto) principal;
        return accountRepository.findByUsername(accountDto.getUsername()).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }

    @Override
    public AccountDto getCurrentAccountDto() {
        return (AccountDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    public AccountDto getAccountDtoFromPrincipal(Principal principal) {
        return (AccountDto) principal;
    }

    @Override
    public Set<Role> getCurrentAccountRoles() {
        return ((AccountDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getRoles();
    }


}
