package com.example.tripmanager.budget.service;

import com.example.tripmanager.budget.model.BudgetTemplate;
import com.example.tripmanager.budget.repository.BudgetTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BudgetTemplateService {
    @Autowired
    private BudgetTemplateRepository budgetTemplateRepository;
    @Autowired
    private BudgetService budgetService;

    public BudgetTemplate createBudgetTemplate(BudgetTemplate budgetTemplate, boolean initialize) {
        BudgetTemplate resultTemplate = budgetTemplateRepository.save(budgetTemplate);
        if (initialize) {
            budgetService.createBudgetFromTemplate(resultTemplate);
        }
        return resultTemplate;
    }
}
