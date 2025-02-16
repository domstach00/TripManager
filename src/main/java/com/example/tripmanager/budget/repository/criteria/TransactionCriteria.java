package com.example.tripmanager.budget.repository.criteria;

import com.example.tripmanager.budget.model.Transaction;
import org.springframework.data.mongodb.core.query.Criteria;

import static com.example.tripmanager.shared.model.AbstractEntity.toObjectId;

public class TransactionCriteria {
    public static Criteria buildCriteriaTransactionWithGivenBudgetId(String budgetId) {
        return Criteria.where(Transaction.FIELD_NAME_BUDGET_ID).is(toObjectId(budgetId));
    }
}
