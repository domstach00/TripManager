package com.example.tripmanager.budget.model;

import com.fasterxml.jackson.annotation.JsonFormat;

public class TransactionBudgetSummary {
    public static final String FIELD_NAME_BUDGET_ID = "budgetId";
    public static final String FIELD_NAME_TRANSACTION_COUNT = "transactionCount";
    public static final String FIELD_NAME_TOTAL_TRANSACTIONS_VALUE = "totalTransactionsValue";

    private String budgetId;
    private int transactionCount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00")
    private double totalTransactionsValue;

    public TransactionBudgetSummary() {
    }

    public TransactionBudgetSummary(String budgetId, int transactionCount, double totalTransactionsValue) {
        this.budgetId = budgetId;
        this.transactionCount = transactionCount;
        this.totalTransactionsValue = totalTransactionsValue;
    }

    public String getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(String budgetId) {
        this.budgetId = budgetId;
    }

    public int getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(int transactionCount) {
        this.transactionCount = transactionCount;
    }

    public double getTotalTransactionsValue() {
        return totalTransactionsValue;
    }

    public void setTotalTransactionsValue(double totalTransactionsValue) {
        this.totalTransactionsValue = totalTransactionsValue;
    }
}
