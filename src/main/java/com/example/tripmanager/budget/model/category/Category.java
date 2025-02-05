package com.example.tripmanager.budget.model.category;

import com.example.tripmanager.budget.model.BudgetType;

import java.math.BigDecimal;
import java.util.List;

public class Category {
    private String symbolicName;
    private String name;
    private BudgetType type;
    private BigDecimal allocatedAmount;
    private List<SubCategory> subCategories;

    public Category(String symbolicName, String name, BudgetType type, BigDecimal allocatedAmount, List<SubCategory> subCategories) {
        this.symbolicName = symbolicName;
        this.name = name;
        this.type = type;
        this.allocatedAmount = allocatedAmount;
        this.subCategories = subCategories;
    }

    public Category() {
    }

    public String getSymbolicName() {
        return symbolicName;
    }

    public void setSymbolicName(String symbolicName) {
        this.symbolicName = symbolicName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BudgetType getType() {
        return type;
    }

    public void setType(BudgetType type) {
        this.type = type;
    }

    public BigDecimal getAllocatedAmount() {
        return allocatedAmount;
    }

    public void setAllocatedAmount(BigDecimal allocatedAmount) {
        this.allocatedAmount = allocatedAmount;
    }

    public List<SubCategory> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<SubCategory> subCategories) {
        this.subCategories = subCategories;
    }
}
