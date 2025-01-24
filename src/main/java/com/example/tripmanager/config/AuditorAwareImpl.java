package com.example.tripmanager.config;

import com.example.tripmanager.shared.model.AbstractEntity;
import com.example.tripmanager.account.model.Account;
import com.example.tripmanager.account.model.AccountDto;
import com.example.tripmanager.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Objects;
import java.util.Optional;

@Configuration
@Lazy
@EnableMongoAuditing
public class AuditorAwareImpl implements AuditorAware<String> {
    @Autowired
    private AccountService accountService;

    @Override
    @NonNull
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (Objects.isNull(authentication)
                || authentication instanceof AnonymousAuthenticationToken
                || !authentication.isAuthenticated()
                || Objects.isNull(authentication.getPrincipal())) {
            return Optional.empty();
        }

        Object principalObj = authentication.getPrincipal();
        String accountId = null;

        if (principalObj instanceof AccountDto accountDto) {
            accountId = accountDto.getId();
        }

        if (Objects.isNull(accountId)) {
            String username = ((UserDetails) principalObj).getUsername();
            Optional<Account> account = Objects.isNull(username)
                    ? Optional.empty()
                    : accountService.getAccountByEmail(username);
            if (account.isPresent()) {
                return account.map(AbstractEntity::getId);
            } else {
                return Optional.ofNullable(username);
            }
        } else {
            return Optional.of(accountId);
        }
    }
}
