package com.example.tripmanager.budget.repository.criteria;

import com.example.tripmanager.budget.model.Transaction;
import org.springframework.data.mongodb.core.query.Criteria;

import static com.example.tripmanager.shared.model.AbstractEntity.toObjectId;

public class TransactionCriteria {
    public static Criteria buildCriteriaTransactionWithGivenBudgetId(String budgetId) {
        return Criteria.where(Transaction.FIELD_NAME_BUDGET_ID).is(toObjectId(budgetId));
    }

    public static Criteria buildCriteriaTransactionWithGivenCategoryId(String categoryId) {
        return Criteria.where(Transaction.FIELD_NAME_CATEGORY_ID).is(toObjectId(categoryId));
    }

    public static Criteria buildCriteriaTransactionWithGivenSubCategoryId(String subCategoryId) {
        return Criteria.where(Transaction.FIELD_NAME_SUB_CATEGORY_ID).is(toObjectId(subCategoryId));
    }

    public static Criteria buildCriteriaFieldExists(String fieldName, boolean exists) {
        if (exists) {
            return Criteria.where(fieldName).ne(null);
        } else {
            return Criteria.where(fieldName).is(null);
        }
    }
}
