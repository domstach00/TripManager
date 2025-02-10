package com.example.tripmanager.budget.model.category;

import com.example.tripmanager.budget.deserializer.JsonBigDecimalDeserializer;
import com.example.tripmanager.budget.model.BudgetType;
import com.example.tripmanager.shared.validation.EnumValid;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public class CategoryCreateForm {
    private String name;
    @EnumValid(enumClass = BudgetType.class, message = "Invalid Budget type.")
    private String type;
    @JsonDeserialize(using = JsonBigDecimalDeserializer.class)
    @Digits(integer = 10, fraction = 2, message = "Amount cannot exceed 10 whole digits and 2 decimal places")
    @PositiveOrZero(message = "Allocated budget cannot be negative number")
    private BigDecimal allocatedAmount;

    public CategoryCreateForm() {
    }

    public CategoryCreateForm(String name, String type, BigDecimal allocatedAmount) {
        this.name = name;
        this.type = type;
        this.allocatedAmount = allocatedAmount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getAllocatedAmount() {
        return allocatedAmount;
    }

    public void setAllocatedAmount(BigDecimal allocatedAmount) {
        this.allocatedAmount = allocatedAmount;
    }
}
