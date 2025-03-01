package com.example.tripmanager.budget.service;

import com.example.tripmanager.budget.model.BudgetTemplate;
import com.example.tripmanager.budget.repository.BudgetTemplateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BudgetTemplateService {
    @Autowired
    private BudgetTemplateRepository budgetTemplateRepository;
    @Autowired
    private BudgetService budgetService;

    public BudgetTemplate createBudgetTemplate(BudgetTemplate budgetTemplate, boolean initialize) {
        if (budgetTemplate == null) {
            log.error("Attempted to create a null budget template");
            throw new IllegalArgumentException("Budget template cannot be null");
        }

        log.debug("Creating a new budget template: {}", budgetTemplate.getName());
        BudgetTemplate resultTemplate = budgetTemplateRepository.save(budgetTemplate);
        log.info("Budget template successfully created with ID: {}", resultTemplate.getId());

        if (initialize) {
            log.debug("Initializing budget from template with ID: {}", resultTemplate.getId());
            budgetService.createBudgetFromTemplate(resultTemplate);
            log.info("Budget successfully initialized from template: {}", resultTemplate.getId());
        }
        return resultTemplate;
    }
}
