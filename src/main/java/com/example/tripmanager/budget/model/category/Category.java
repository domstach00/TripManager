package com.example.tripmanager.budget.model.category;

import com.example.tripmanager.budget.model.BudgetType;
import com.example.tripmanager.shared.model.AbstractAuditable;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Document(collection = Category.COLLECTION_NAME)
public class Category extends AbstractAuditable {
    public static final String COLLECTION_NAME = "Categories";
    public static final String FIELD_NAME_NAME = "name";
    public static final String FIELD_NAME_TYPE = "type";
    public static final String FIELD_NAME_ALLOCATED_AMOUNT = "allocatedAmount";
    public static final String FIELD_NAME_SUB_CATEGORIES = "subCategories";
    private String name;
    private BudgetType type;
    private BigDecimal allocatedAmount;
    private List<SubCategory> subCategories;

    public Category(String name, BudgetType type, BigDecimal allocatedAmount, List<SubCategory> subCategories) {
        this.name = name;
        this.type = type;
        this.allocatedAmount = allocatedAmount;
        this.subCategories = subCategories;
    }

    public Category() {
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
        if (subCategories == null) {
            subCategories = new ArrayList<>();
        }
        return subCategories;
    }

    public void setSubCategories(List<SubCategory> subCategories) {
        this.subCategories = subCategories;
    }

    public void addSubCategory(SubCategory subCategory) {
        getSubCategories().add(subCategory);
    }
}
