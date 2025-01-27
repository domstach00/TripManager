package com.example.tripmanager.budget.model;

import com.example.tripmanager.budget.deserializer.JsonBigDecimalDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.Digits;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class BudgetCreateForm {
    private List<String> membersToInvite;
    private String name;
    private String description;
    @JsonDeserialize(using = JsonBigDecimalDeserializer.class)
    @Digits(integer = 10, fraction = 2, message = "Amount cannot exceed 10 whole digits and 2 decimal places")
    private BigDecimal allocatedBudget;
    private LocalDate startDate;
    private LocalDate endDate;
}
