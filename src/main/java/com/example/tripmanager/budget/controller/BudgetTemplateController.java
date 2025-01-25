package com.example.tripmanager.budget.controller;

import com.example.tripmanager.account.model.Account;
import com.example.tripmanager.budget.mapper.BudgetTemplateMapper;
import com.example.tripmanager.budget.model.BudgetTemplate;
import com.example.tripmanager.budget.model.BudgetTemplateDto;
import com.example.tripmanager.budget.service.BudgetTemplateService;
import com.example.tripmanager.shared.controller.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping(BudgetTemplateController.CONTROLLER_URL)
public class BudgetTemplateController extends AbstractController {
    public static final String CONTROLLER_URL = "/api/budget/template";

    @Autowired
    private BudgetTemplateService budgetTemplateService;

    protected BudgetTemplateDto toDto(BudgetTemplate budgetTemplate) {
        return BudgetTemplateMapper.toDto(budgetTemplate, accountService);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BudgetTemplateDto createBudgetTemplate(
            Principal principal,
            BudgetTemplateDto budgetTemplateDto
    ) {
        Account currentAccount = getCurrentAccount(principal);
        BudgetTemplate budgetTemplateFromCreate = BudgetTemplateMapper.createFromDto(budgetTemplateDto);
        BudgetTemplate result = budgetTemplateService.createBudgetTemplate(budgetTemplateFromCreate);
        return toDto(result);
    }
}
