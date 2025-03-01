package com.example.tripmanager.shared.controller;


import com.example.tripmanager.account.model.Account;
import com.example.tripmanager.account.service.AccountService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.Principal;
import java.util.Objects;

public abstract class AbstractController {
    @Autowired
    protected AccountService accountService;

    protected Account getCurrentAccount(Principal principal) {
        if (principal == null) {
            throw new IllegalArgumentException("Principal cannot be null");
        }
        return accountService.getCurrentAccount(principal);
    }
    public void throwErrorOnValidateIdsFromUrlAndBody(String idFromUrl, String idFromBody) {
        if (StringUtils.isBlank(idFromUrl) || StringUtils.isBlank(idFromBody)) {
            throw new IllegalArgumentException("Id cannot be blank or null");
        }
        if (!Objects.equals(idFromUrl, idFromBody)) {
            throw new IllegalArgumentException("Id in path and given body does not match");
        }
    }
}
