package com.example.tripmanager.budget.controller;

import com.example.tripmanager.account.model.Account;
import com.example.tripmanager.budget.mapper.BudgetMapper;
import com.example.tripmanager.budget.model.Budget;
import com.example.tripmanager.budget.model.BudgetCreateForm;
import com.example.tripmanager.budget.model.BudgetDto;
import com.example.tripmanager.budget.service.BudgetService;
import com.example.tripmanager.shared.controller.AbstractController;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping(BudgetController.CONTROLLER_URL)
public class BudgetController extends AbstractController {
    public static final String CONTROLLER_URL = "/api/budget";

    @Autowired
    private BudgetService budgetService;

    protected BudgetDto toDto(Budget budget) {
        return BudgetMapper.toDto(budget, accountService);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BudgetDto createBudget(
            Principal principal,
            @Valid @RequestBody BudgetCreateForm budgetCreateForm
    ) {
        Account currentAccount = getCurrentAccount(principal);
        Budget createdBudget = budgetService.createBudget(budgetCreateForm, currentAccount);
        return toDto(createdBudget);
    }

}
