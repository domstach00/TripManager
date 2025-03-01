package com.example.tripmanager.auth.security.service;

import com.example.tripmanager.account.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return accountRepository.findByEmail(email)
                .map(account -> new User(account.getEmail(), account.getPassword(), account.getAuthorities()))
                .orElseThrow(() -> {
                    log.error("User '{}' not found", email);
                    return new UsernameNotFoundException("Username not found in CustomUserDetailsService");
                });
    }
}
