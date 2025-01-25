package com.example.tripmanager.budget.service;

import com.example.tripmanager.budget.model.BudgetTemplate;
import com.example.tripmanager.budget.repository.BudgetTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BudgetTemplateService {
    @Autowired
    private BudgetTemplateRepository budgetTemplateRepository;

    public BudgetTemplate createBudgetTemplate(BudgetTemplate budgetTemplate) {
        return budgetTemplateRepository.save(budgetTemplate);
    }
}
