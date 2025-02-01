package com.example.tripmanager.budget.controller;

import com.example.tripmanager.account.model.Account;
import com.example.tripmanager.budget.mapper.BudgetMapper;
import com.example.tripmanager.budget.model.Budget;
import com.example.tripmanager.budget.model.BudgetCreateForm;
import com.example.tripmanager.budget.model.BudgetDto;
import com.example.tripmanager.budget.service.BudgetService;
import com.example.tripmanager.shared.controller.AbstractController;
import com.example.tripmanager.shared.controller.support.PageParams;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping(BudgetController.CONTROLLER_URL)
public class BudgetController extends AbstractController {
    public static final String CONTROLLER_URL = "/api/budgets";

    @Autowired
    private BudgetService budgetService;

    protected BudgetDto toDto(Budget budget) {
        return BudgetMapper.toDto(budget, accountService);
    }

    protected Page<BudgetDto> toDto(Page<Budget> budgets) {
        return BudgetMapper.toDto(budgets, accountService);
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

    @GetMapping
    public Page<BudgetDto> getBudgets(
            Principal principal,
            @ParameterObject PageParams pageParams,
            @RequestParam(required = false, defaultValue = "false") boolean includeArchived
    ) {
        Account currentAccount = getCurrentAccount(principal);
        Page<Budget> budgets = budgetService.getBudgetsRelatedToAccount(pageParams.asPageable(), currentAccount, includeArchived);
        return toDto(budgets);
    }

    @GetMapping("/{budgetId}")
    public BudgetDto getBudget(
            Principal principal,
            @PathVariable String budgetId
    ) {
        Account currentAccount = getCurrentAccount(principal);
        Budget budget = budgetService.getBudgetById(budgetId, currentAccount);
        return toDto(budget);
    }
}
