package com.example.tripmanager.budget.model;

import com.example.tripmanager.account.model.AccountDto;
import com.example.tripmanager.budget.model.category.Category;
import com.example.tripmanager.shared.model.AbstractAuditableDto;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.List;

public class BudgetTemplateDto extends AbstractAuditableDto {
    private AccountDto ownerId;
    private boolean isPublic;
    private String name;
    private String description;
    private List<Category> categories;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00")
    private BigDecimal allocatedBudget;
    private String budgetPeriod;
    private boolean isArchived;

    public BudgetTemplateDto(AccountDto ownerId, boolean isPublic, String name, String description, List<Category> categories, BigDecimal allocatedBudget, String budgetPeriod, boolean isArchived) {
        this.ownerId = ownerId;
        this.isPublic = isPublic;
        this.name = name;
        this.description = description;
        this.categories = categories;
        this.allocatedBudget = allocatedBudget;
        this.budgetPeriod = budgetPeriod;
        this.isArchived = isArchived;
    }

    public BudgetTemplateDto() {
    }

    public AccountDto getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(AccountDto ownerId) {
        this.ownerId = ownerId;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public BigDecimal getAllocatedBudget() {
        return allocatedBudget;
    }

    public void setAllocatedBudget(BigDecimal allocatedBudget) {
        this.allocatedBudget = allocatedBudget;
    }

    public String getBudgetPeriod() {
        return budgetPeriod;
    }

    public void setBudgetPeriod(String budgetPeriod) {
        this.budgetPeriod = budgetPeriod;
    }

    public boolean isArchived() {
        return isArchived;
    }

    public void setArchived(boolean archived) {
        isArchived = archived;
    }
}
