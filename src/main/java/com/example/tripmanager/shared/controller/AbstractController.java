package com.example.tripmanager.shared.controller;

import com.example.tripmanager.account.model.Account;
import com.example.tripmanager.account.service.AccountService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.Principal;
import java.util.Objects;

public abstract class AbstractController {
    private static final Logger log = LoggerFactory.getLogger(AbstractController.class);
    @Autowired
    protected AccountService accountService;

    protected Account getCurrentAccount(Principal principal) {
        if (principal == null) {
            log.debug("Principal is null");
            throw new IllegalArgumentException("Principal cannot be null");
        }
        return accountService.getCurrentAccount(principal);
    }
    public void throwErrorOnValidateIdsFromUrlAndBody(String idFromUrl, String idFromBody) {
        if (StringUtils.isBlank(idFromUrl) || StringUtils.isBlank(idFromBody)) {
            throw new IllegalArgumentException("Id cannot be blank or null");
        }
        if (!Objects.equals(idFromUrl, idFromBody)) {
            log.warn("Id in path and given body does now match");
            throw new IllegalArgumentException("Id in path and given body does not match");
        }
    }
}
