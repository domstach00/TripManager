package com.example.tripmanager.budget.model.category;

public class SubCategory {
    private String symbolicName;
    private String name;

    public SubCategory(String symbolicName, String name) {
        this.symbolicName = symbolicName;
        this.name = name;
    }

    public SubCategory() {

    }

    public String getSymbolicName() {
        return symbolicName;
    }

    public void setSymbolicName(String symbolicName) {
        this.symbolicName = symbolicName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
