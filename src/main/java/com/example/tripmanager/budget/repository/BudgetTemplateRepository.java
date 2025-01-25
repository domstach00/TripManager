package com.example.tripmanager.budget.repository;

import com.example.tripmanager.budget.model.BudgetTemplate;
import com.example.tripmanager.shared.repository.AbstractRepositoryImpl;
import org.springframework.stereotype.Repository;

@Repository
public class BudgetTemplateRepository extends AbstractRepositoryImpl<BudgetTemplate> {
    @Override
    protected Class<BudgetTemplate> getEntityClass() {
        return BudgetTemplate.class;
    }


}
