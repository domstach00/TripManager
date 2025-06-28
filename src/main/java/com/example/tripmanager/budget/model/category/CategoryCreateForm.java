package com.example.tripmanager.budget.model.category;

import com.example.tripmanager.budget.deserializer.JsonBigDecimalDeserializer;
import com.example.tripmanager.budget.model.BudgetType;
import com.example.tripmanager.shared.util.ColorUtils;
import com.example.tripmanager.shared.validation.EnumValid;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Pattern;
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
    @Pattern(regexp = ColorUtils.HEX_COLOR_REGEX, message = ColorUtils.HEX_COLOR_ERROR_MESSAGE)
    private String color;

    public CategoryCreateForm() {
    }

    public CategoryCreateForm(String name, String type, BigDecimal allocatedAmount, String color) {
        this.name = name;
        this.type = type;
        this.allocatedAmount = allocatedAmount;
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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
