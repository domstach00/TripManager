package com.example.tripmanager.budget.security;

import com.example.tripmanager.account.model.Account;
import com.example.tripmanager.account.service.AccountService;
import com.example.tripmanager.budget.service.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component("budgetSecurity")
public class BudgetSecurityService {
    private final AccountService accountService;
    private final BudgetService budgetService;

    @Autowired
    public BudgetSecurityService(AccountService accountService, BudgetService budgetService) {
        this.accountService = accountService;
        this.budgetService = budgetService;
    }


    public boolean hasAccessToBudget(Principal principal, String budgetId) {
        Account account = accountService.getCurrentAccount(principal);
        return budgetService.getBudgetById(budgetId, account)
                .isPresent();
    }
}
