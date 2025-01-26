package com.example.tripmanager.budget.model;

import com.example.tripmanager.account.model.AccountDto;
import com.example.tripmanager.budget.model.category.Category;
import com.example.tripmanager.shared.model.AbstractAuditableDto;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.List;

public class BudgetDto extends AbstractAuditableDto {
    private AccountDto ownerId;
    private List<AccountDto> members;
    private String templateId;
    private String name;
    private List<Category> categories;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00")
    private BigDecimal allocatedBudget;
    private String startDate;
    private String endDate;
    private boolean isArchived;

    public BudgetDto(AccountDto ownerId, List<AccountDto> members, String templateId, String name, List<Category> categories, String description, BigDecimal allocatedBudget, String startDate, String endDate, boolean isArchived) {
        this.ownerId = ownerId;
        this.members = members;
        this.templateId = templateId;
        this.name = name;
        this.categories = categories;
        this.description = description;
        this.allocatedBudget = allocatedBudget;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isArchived = isArchived;
    }

    public BudgetDto() {
    }

    public AccountDto getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(AccountDto ownerId) {
        this.ownerId = ownerId;
    }

    public List<AccountDto> getMembers() {
        return members;
    }

    public void setMembers(List<AccountDto> members) {
        this.members = members;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAllocatedBudget() {
        return allocatedBudget;
    }

    public void setAllocatedBudget(BigDecimal allocatedBudget) {
        this.allocatedBudget = allocatedBudget;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public boolean isArchived() {
        return isArchived;
    }

    public void setArchived(boolean archived) {
        isArchived = archived;
    }
}
