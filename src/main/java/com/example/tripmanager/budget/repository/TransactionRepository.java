package com.example.tripmanager.budget.repository;

import com.example.tripmanager.budget.model.Transaction;
import com.example.tripmanager.budget.model.TransactionBudgetSummary;
import com.example.tripmanager.shared.repository.AbstractRepositoryImpl;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.tripmanager.budget.repository.criteria.TransactionCriteria.*;

@Repository
public class TransactionRepository extends AbstractRepositoryImpl<Transaction> {
    @Override
    protected Class<Transaction> getEntityClass() {
        return Transaction.class;
    }

    public Optional<TransactionBudgetSummary> getTransactionBudgetSummaryByBudgetId(String budgetId) {
        List<AggregationOperation> operationList = new ArrayList<>();
        operationList.add(
                Aggregation.match(
                        buildCriteriaTransactionWithGivenBudgetId(budgetId)
                )
        );

        final String tmpAmountDoubleType = "_tmpAmountDoubleType";

        operationList.add(
                Aggregation.project()
                        .and(ConvertOperators.valueOf(Transaction.FIELD_NAME_AMOUNT).convertToDouble()) // Convert String to Double
                        .as(tmpAmountDoubleType)
        );

        operationList.add(
                Aggregation.group(Transaction.FIELD_NAME_BUDGET_ID)
                        .count().as(TransactionBudgetSummary.FIELD_NAME_TRANSACTION_COUNT)
                        .sum(tmpAmountDoubleType).as(TransactionBudgetSummary.FIELD_NAME_TOTAL_TRANSACTIONS_VALUE)
        );
        operationList.add(
                Aggregation.project()
                        .and(FIELD_NAME_ID_WITH_UNDERSCORE).as(TransactionBudgetSummary.FIELD_NAME_BUDGET_ID)
                        .and(TransactionBudgetSummary.FIELD_NAME_TRANSACTION_COUNT).as(TransactionBudgetSummary.FIELD_NAME_TRANSACTION_COUNT)
                        .and(TransactionBudgetSummary.FIELD_NAME_TOTAL_TRANSACTIONS_VALUE).as(TransactionBudgetSummary.FIELD_NAME_TOTAL_TRANSACTIONS_VALUE)
        );

        return findOneBy(operationList, Transaction.class, TransactionBudgetSummary.class);
    }

}
