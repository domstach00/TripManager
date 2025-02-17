package com.example.tripmanager.budget.model.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SubCategoryCreateForm {
    @NotBlank
    @Size(max = 50)
    private String name;

    public SubCategoryCreateForm() {
    }

    public SubCategoryCreateForm(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
