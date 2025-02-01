package com.example.tripmanager.budget.model;

import com.example.tripmanager.budget.deserializer.JsonBigDecimalDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class BudgetCreateForm {
    private List<String> membersToInvite;
    @NotBlank(message = "Budget name cannot be blank")
    private String name;
    private String description;
    @JsonDeserialize(using = JsonBigDecimalDeserializer.class)
    @Digits(integer = 10, fraction = 2, message = "Amount cannot exceed 10 whole digits and 2 decimal places")
    @PositiveOrZero(message = "Allocated budget cannot be negative number")
    private BigDecimal allocatedBudget;
    private LocalDate startDate;
    private LocalDate endDate;

    @AssertTrue(message = "Start date has to be earlier then end date")
    public  boolean isDateOrderValid(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return true;
        }
        return !endDate.isBefore(startDate);
    }

    public List<String> getMembersToInvite() {
        return membersToInvite;
    }

    public void setMembersToInvite(List<String> membersToInvite) {
        this.membersToInvite = membersToInvite;
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

    public BigDecimal getAllocatedBudget() {
        return allocatedBudget;
    }

    public void setAllocatedBudget(BigDecimal allocatedBudget) {
        this.allocatedBudget = allocatedBudget;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
