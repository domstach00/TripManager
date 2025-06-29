package com.example.tripmanager.budget.model.category;

import com.example.tripmanager.budget.model.BudgetType;

import java.math.BigDecimal;
import java.util.List;

public class CategoryWithStats extends Category {
    private BigDecimal totalSpentAmount;

    public CategoryWithStats() {
        super();
    }

    public CategoryWithStats(Category category, BigDecimal totalSpentAmount) {
        super(category.getName(), category.getType(), category.getAllocatedAmount(), category.getSubCategories(), category.getColor());
        this.setId(category.getId());
        this.setLastModifiedTime(category.getLastModifiedTime());
        this.setLastModifiedBy(category.getLastModifiedBy());
        this.totalSpentAmount = totalSpentAmount;
    }

    public static CategoryWithStats uncategorizedWithStats(BudgetType budgetType, BigDecimal totalAmount) {
        Category category = new Category("NoCategory", budgetType, BigDecimal.ZERO, List.of(), "#000000");
        return new CategoryWithStats(category, totalAmount);
    }

    public BigDecimal getTotalSpentAmount() {
        return totalSpentAmount;
    }

    public void setTotalSpentAmount(BigDecimal totalSpentAmount) {
        this.totalSpentAmount = totalSpentAmount;
    }
}
