package com.example.tripmanager.budget.mapper;

import com.example.tripmanager.budget.model.Budget;
import com.example.tripmanager.budget.model.BudgetTemplate;
import org.bson.types.ObjectId;

import java.time.Instant;

public class BudgetMapper {

    public static Budget budgetFromTemplate(BudgetTemplate budgetTemplate) {
        Budget budget = new Budget();
        budget.setOwnerId(budgetTemplate.getOwnerId());
        budget.setTemplateId(new ObjectId(budgetTemplate.getId()));
        budget.setName(budgetTemplate.getName());
        budget.setCategories(budgetTemplate.getCategories());
        budget.setDescription(budgetTemplate.getDescription());
        budget.setAllocatedBudget(budgetTemplate.getAllocatedBudget());
        budget.setArchived(false);

        budget.setStartDate(Instant.now());
        BudgetTemplate.BudgetPeriod budgetPeriod = budgetTemplate.getBudgetPeriod();
        if (budgetPeriod != null) {
            Instant endDate = budgetPeriod.calculateEndDate(budget.getStartDate());
            budget.setEndDate(endDate);
        }
        return budget;
    }
}
