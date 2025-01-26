package com.example.tripmanager.budget.service;

import com.example.tripmanager.budget.mapper.BudgetMapper;
import com.example.tripmanager.budget.model.Budget;
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
}
