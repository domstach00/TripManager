package com.example.tripmanager.budget.model;

import com.example.tripmanager.shared.model.AbstractAuditable;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;

@Document(collection = Transaction.COLLECTION_NAME)
public class Transaction extends AbstractAuditable {
    public static final String COLLECTION_NAME = "Transactions";
    public static final String FIELD_NAME_BUDGET_ID = "budgetId";
    public static final String FIELD_NAME_CATEGORY_ID = "categoryId";
    public static final String FIELD_NAME_SUB_CATEGORY_ID = "subCategoryId";
    public static final String FIELD_NAME_DESCRIPTION = "description";
    public static final String FIELD_NAME_AMOUNT = "amount";
    public static final String FIELD_NAME_TRANSACTION_DATE = "transactionDate";

    private ObjectId budgetId;
    private ObjectId categoryId;
    private ObjectId subCategoryId;
    private String description;
    @NotNull(message = "Amount cannot be null")
    @PositiveOrZero(message = "Amount cannot be negative")
    @Digits(integer = 10, fraction = 2, message = "Amount cannot exceed 10 whole digits and 2 decimal places")
    private BigDecimal amount;
    private Instant transactionDate;

    public Transaction(ObjectId budgetId, ObjectId categoryId, ObjectId subCategoryId, String description, BigDecimal amount, Instant transactionDate) {
        this.budgetId = budgetId;
        this.categoryId = categoryId;
        this.subCategoryId = subCategoryId;
        this.description = description;
        this.amount = amount;
        this.transactionDate = transactionDate;
    }

    public Transaction() {
    }

    public ObjectId getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(ObjectId budgetId) {
        this.budgetId = budgetId;
    }

    public ObjectId getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(ObjectId categoryId) {
        this.categoryId = categoryId;
    }

    public ObjectId getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(ObjectId subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Instant getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Instant transactionDate) {
        this.transactionDate = transactionDate;
    }
}
