package com.example.tripmanager.controller;


import com.example.tripmanager.model.account.Account;
import com.example.tripmanager.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.Principal;
import java.util.Objects;

public abstract class AbstractController {
    @Autowired
    protected AccountService accountService;

    protected Account getCurrentAccount(Principal principal) {
        return accountService.getCurrentAccount(principal);
    }
    protected void throwErrorOnValidateIdsFromUrlAndBody(String idFromUrl, String idFromBody) {
        if (!Objects.equals(idFromUrl, idFromBody)) {
            throw new IllegalArgumentException("Id in path and given body does not match");
        }
    }
}
