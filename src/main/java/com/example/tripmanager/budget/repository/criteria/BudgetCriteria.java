package com.example.tripmanager.budget.repository.criteria;

import com.example.tripmanager.budget.model.Budget;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.example.tripmanager.shared.model.AbstractEntity.toObjectId;

public class BudgetCriteria {
    private BudgetCriteria() {}

    public static Criteria buildCriteriaAccountIsBudgetOwner(String accountId) {
        return Criteria.where(Budget.FIELD_NAME_OWNER_ID).is(toObjectId(accountId));
    }

    public static Criteria buildCriteriaAccountIsBudgetMember(String accountId) {
        return Criteria.where(Budget.FIELD_NAME_MEMBERS).is(toObjectId(accountId));
    }

    public static Criteria buildCriteriaAccountIsBudgetMemberOrOwner(String accountId) {
        return new Criteria().orOperator(
                buildCriteriaAccountIsBudgetOwner(accountId),
                buildCriteriaAccountIsBudgetMember(accountId)
        );
    }

    public static Criteria buildCriteriaByAccessModifiers(
            @Nullable Boolean isDeleted,
            @Nullable Boolean isArchived
    ) {
        List<Criteria> criteriaList = new ArrayList<>();

        if (isDeleted != null) {
            criteriaList.add(buildCriteriaIsDeleted(isDeleted));
        }
        if (isArchived != null) {
            criteriaList.add(buildCriteriaIsArchived(isArchived));
        }

        return criteriaList.isEmpty()
                ? new Criteria()
                : new Criteria().andOperator(criteriaList.toArray(new Criteria[0]));
    }

    public static Criteria buildCriteriaIsDeleted(boolean isDeleted) {
        return buildCriteriaByFlag(Budget.FIELD_NAME_IS_DELETED, isDeleted);
    }

    public static Criteria buildCriteriaIsArchived(boolean isArchived) {
        return buildCriteriaByFlag(Budget.FIELD_NAME_IS_ARCHIVED, isArchived);
    }


    private static Criteria buildCriteriaByFlag(String fieldName, boolean flag) {
        return flag
                ? Criteria.where(fieldName).is(true)
                : Criteria.where(fieldName).ne(true);
    }
}
