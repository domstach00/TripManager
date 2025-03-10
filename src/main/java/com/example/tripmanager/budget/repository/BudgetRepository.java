package com.example.tripmanager.budget.repository;

import com.example.tripmanager.account.model.Account;
import com.example.tripmanager.budget.model.Budget;
import com.example.tripmanager.shared.model.AbstractEntity;
import com.example.tripmanager.shared.repository.AbstractRepositoryImpl;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.tripmanager.budget.repository.criteria.BudgetCriteria.buildCriteriaAccountIsBudgetMember;
import static com.example.tripmanager.budget.repository.criteria.BudgetCriteria.buildCriteriaAccountIsBudgetMemberOrOwner;
import static com.example.tripmanager.budget.repository.criteria.BudgetCriteria.buildCriteriaAccountIsBudgetOwner;
import static com.example.tripmanager.budget.repository.criteria.BudgetCriteria.buildCriteriaByAccessModifiers;

@Repository
public class BudgetRepository extends AbstractRepositoryImpl<Budget> {
    @Override
    protected Class<Budget> getEntityClass() {
        return Budget.class;
    }

    public Page<Budget> getBudgetRelatedWhereGivenAccountIsMember(Pageable pageable, Account account, boolean includeArchived) {
        List<AggregationOperation> operationList = new ArrayList<>();
        operationList.add(
                Aggregation.match(
                        buildCriteriaAccountIsBudgetMemberOrOwner(account.getId())
                )
        );

        operationList.add(
                Aggregation.match(
                    buildCriteriaByAccessModifiers(false, includeArchived)
                )
        );

        return findAllBy(pageable, operationList);
    }

    public Optional<Budget> getBudgetById(String budgetId, @NonNull Account currentAccount) {
        return getBudgetById(budgetId, currentAccount.getId());
    }

    public Optional<Budget> getBudgetById(String budgetId, String currentAccountId) {
        List<AggregationOperation> operationList = new ArrayList<>();
        operationList.add(
                Aggregation.match(buildCriteriaById(budgetId))
        );
        operationList.add(
                Aggregation.match(buildCriteriaByAccessModifiers(false, null))
        );
        operationList.add(
                Aggregation.match(
                        buildCriteriaAccountIsBudgetMemberOrOwner(currentAccountId)
                )
        );
        return findOneBy(operationList);
    }

    public Optional<Budget> getBudgetByIdWhereAccountIsOwner(String budgetId, @NonNull Account currentAccount) {
        List<AggregationOperation> operationList = new ArrayList<>();
        operationList.add(
                Aggregation.match(buildCriteriaById(budgetId))
        );
        operationList.add(
                Aggregation.match(buildCriteriaByAccessModifiers(false, null))
        );
        operationList.add(
                Aggregation.match(
                        buildCriteriaAccountIsBudgetOwner(currentAccount.getId())
                )
        );
        return findOneBy(operationList);
    }

    public Optional<Budget> getBudgetByIdWhereAccountIsMember(String budgetId, Account currentAccount) {
        List<AggregationOperation> operationList = new ArrayList<>();
        operationList.add(
                Aggregation.match(buildCriteriaById(budgetId))
        );
        operationList.add(
                Aggregation.match(buildCriteriaByAccessModifiers(false, null))
        );
        operationList.add(
                Aggregation.match(
                        buildCriteriaAccountIsBudgetMember(currentAccount.getId())
                )
        );
        return findOneBy(operationList);
    }

    public void removeCategoryFromBudget(String categoryId) {
        ObjectId categoryObjectId = AbstractEntity.toObjectId(categoryId);
        Query query = new Query();
        query.addCriteria(Criteria.where(Budget.FIELD_NAME_CATEGORIES + "." + FIELD_NAME_ID_MONGO_DB_REF).is(categoryObjectId));

        Query queryToSelectReferenceById = Query.query(Criteria.where(FIELD_NAME_ID_MONGO_DB_REF).is(categoryObjectId));
        Update update = new Update().pull(Budget.FIELD_NAME_CATEGORIES, queryToSelectReferenceById);

        getMongoOperations().updateMulti(query, update, getEntityClass());
    }
}
