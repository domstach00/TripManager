package com.example.tripmanager.budget.model;

import com.example.tripmanager.budget.deserializer.JsonBigDecimalDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionCreateForm {
    @NotBlank
    private String budgetId;
    private String categoryId;
    private String subCategoryId;
    private String description;
    @JsonDeserialize(using = JsonBigDecimalDeserializer.class)
    @Digits(integer = 10, fraction = 2, message = "Amount cannot exceed 10 whole digits and 2 decimal places")
    @PositiveOrZero(message = "Allocated budget cannot be negative number")
    private BigDecimal amount;
    private LocalDateTime transactionDate;

    @AssertTrue(message = "SubCategoryId must exist only if CategoryId exists")
    public boolean isValidSubCategory() {
        return subCategoryId == null || categoryId != null;
    }

    public TransactionCreateForm(String budgetId, String categoryId, String subCategoryId, String description, BigDecimal amount, LocalDateTime transactionDate) {
        this.budgetId = budgetId;
        this.categoryId = categoryId;
        this.subCategoryId = subCategoryId;
        this.description = description;
        this.amount = amount;
        this.transactionDate = transactionDate;
    }

    public TransactionCreateForm() {
    }

    public String getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(String budgetId) {
        this.budgetId = budgetId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(String subCategoryId) {
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

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }
}
