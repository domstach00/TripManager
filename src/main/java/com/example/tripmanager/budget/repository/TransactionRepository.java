package com.example.tripmanager.budget.repository;

import com.example.tripmanager.budget.model.Transaction;
import com.example.tripmanager.budget.model.TransactionBudgetSummary;
import com.example.tripmanager.shared.repository.AbstractRepositoryImpl;
import com.mongodb.client.result.UpdateResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.ConvertOperators;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.tripmanager.budget.repository.criteria.TransactionCriteria.buildCriteriaFieldExists;
import static com.example.tripmanager.budget.repository.criteria.TransactionCriteria.buildCriteriaTransactionWithGivenBudgetId;
import static com.example.tripmanager.budget.repository.criteria.TransactionCriteria.buildCriteriaTransactionWithGivenCategoryId;
import static com.example.tripmanager.budget.repository.criteria.TransactionCriteria.buildCriteriaTransactionWithGivenSubCategoryId;

@Repository
public class TransactionRepository extends AbstractRepositoryImpl<Transaction> {
    @Override
    protected Class<Transaction> getEntityClass() {
        return Transaction.class;
    }

    public BigDecimal getTotalAmountForCategory(String budgetId, String categoryId) {
        List<AggregationOperation> operationList = new ArrayList<>();
        operationList.add(Aggregation.match(buildCriteriaTransactionWithGivenBudgetId(budgetId)));
        operationList.add(Aggregation.match(buildCriteriaTransactionWithGivenCategoryId(categoryId)));

        final String tmpAmountDoubleType = "_tmpAmountDoubleType";

        operationList.add(
                Aggregation.project()
                        .and(ConvertOperators.valueOf(Transaction.FIELD_NAME_AMOUNT).convertToDouble())
                        .as(tmpAmountDoubleType)
        );

        operationList.add(
                Aggregation.group()
                        .sum(tmpAmountDoubleType).as("totalAmount")
        );

        List<org.bson.Document> results = getMongoOperations().aggregate(
                Aggregation.newAggregation(operationList),
                Transaction.class,
                org.bson.Document.class
        ).getMappedResults();

        if (results.isEmpty() || results.get(0).get("totalAmount") == null) {
            return BigDecimal.ZERO;
        }

        return new BigDecimal(results.get(0).get("totalAmount").toString());
    }

    public Page<Transaction> getTransactionByBudgetIdAndCategoryId(Pageable pageable, String budgetId, @Nullable String categoryId, @Nullable String subCategoryId, boolean excludeCategorized, boolean excludeSubCategorized) {
        List<AggregationOperation> operationList = new ArrayList<>();
        operationList.add(
                Aggregation.match(
                        buildCriteriaTransactionWithGivenBudgetId(budgetId)
                )
        );

        if (categoryId != null && !excludeCategorized) {
            operationList.add(
                    Aggregation.match(
                            buildCriteriaTransactionWithGivenCategoryId(categoryId)
                    )
            );
        } else if (excludeCategorized) {
            operationList.add(
                    Aggregation.match(
                            buildCriteriaFieldExists(Transaction.FIELD_NAME_CATEGORY_ID, false)
                    )
            );
        }

        if (subCategoryId != null && !excludeSubCategorized) {
            operationList.add(
                    Aggregation.match(
                            buildCriteriaTransactionWithGivenSubCategoryId(subCategoryId)
                    )
            );
        } else if (excludeSubCategorized) {
            operationList.add(
                    Aggregation.match(
                            buildCriteriaFieldExists(Transaction.FIELD_NAME_SUB_CATEGORY_ID, false)
                    )
            );
        }

        return findAllBy(pageable, operationList);
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

    public UpdateResult deleteCategoryFromTransactionsInBudget(String categoryId) {
        Query query = new Query();
        query.addCriteria(buildCriteriaTransactionWithGivenCategoryId(categoryId));

        Update update = new Update()
                .unset(Transaction.FIELD_NAME_CATEGORY_ID)
                .unset(Transaction.FIELD_NAME_SUB_CATEGORY_ID);
        return getMongoOperations().updateMulti(query, update, Transaction.class);
    }
}
