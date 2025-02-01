package com.example.tripmanager.budget.service;

import com.example.tripmanager.account.model.Account;
import com.example.tripmanager.budget.mapper.BudgetMapper;
import com.example.tripmanager.budget.model.Budget;
import com.example.tripmanager.budget.model.BudgetCreateForm;
import com.example.tripmanager.budget.model.BudgetTemplate;
import com.example.tripmanager.budget.repository.BudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BudgetService {
    @Autowired
    private BudgetRepository budgetRepository;

    public Budget createBudgetFromTemplate(BudgetTemplate budgetTemplate) {
        Budget budget = BudgetMapper.budgetFromTemplate(budgetTemplate);
        return budgetRepository.save(budget);
    }

    public Budget createBudget(BudgetCreateForm budgetCreateForm, Account currentAccount) {
        Budget budgetToCreate = BudgetMapper.budgetFromCreateForm(budgetCreateForm, currentAccount);
        Budget savedBudget = budgetRepository.save(budgetToCreate);

        // TODO: Invite members to created budget

        return savedBudget;
    }
}
