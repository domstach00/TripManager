package com.example.tripmanager.budget.repository;

import com.example.tripmanager.budget.model.Budget;
import com.example.tripmanager.shared.repository.AbstractRepositoryImpl;
import org.springframework.stereotype.Repository;

@Repository
public class BudgetRepository extends AbstractRepositoryImpl<Budget> {
    @Override
    protected Class<Budget> getEntityClass() {
        return Budget.class;
    }


}
