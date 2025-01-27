package com.example.tripmanager.budget.model;

import com.example.tripmanager.budget.deserializer.JsonBigDecimalDeserializer;
import com.example.tripmanager.shared.validation.EnumValid;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.Digits;

import java.math.BigDecimal;

public class BudgetTemplateCreateForm {
    private String name;
    private String description;
    @JsonDeserialize(using = JsonBigDecimalDeserializer.class)
    @Digits(integer = 10, fraction = 2, message = "Amount cannot exceed 10 whole digits and 2 decimal places")
    private BigDecimal allocatedBudget;
    @EnumValid(enumClass = BudgetTemplate.BudgetPeriod.class, message = "Invalid budget period.")
    private String budgetPeriod;
    private boolean isPublic;
}
