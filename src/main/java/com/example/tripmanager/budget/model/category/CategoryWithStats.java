package com.example.tripmanager.budget.model.category;

import java.math.BigDecimal;

public class CategoryWithStats extends Category {
    private BigDecimal totalSpentAmount;

    public CategoryWithStats() {
        super();
    }

    public CategoryWithStats(Category category, BigDecimal totalSpentAmount) {
        super(category.getName(), category.getType(), category.getAllocatedAmount(), category.getSubCategories(), category.getColor());
        this.setId(category.getId());
        this.setCreatedTime(category.getCreatedTime());
        this.setLastModifiedTime(category.getLastModifiedTime());
        this.setCreatedBy(category.getCreatedBy());
        this.setLastModifiedBy(category.getLastModifiedBy());
        this.totalSpentAmount = totalSpentAmount;
    }

    public BigDecimal getTotalSpentAmount() {
        return totalSpentAmount;
    }

    public void setTotalSpentAmount(BigDecimal totalSpentAmount) {
        this.totalSpentAmount = totalSpentAmount;
    }
}
