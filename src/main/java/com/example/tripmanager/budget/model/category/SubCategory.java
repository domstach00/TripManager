package com.example.tripmanager.budget.model.category;

import org.bson.types.ObjectId;

public class SubCategory {
    private String id;
    private String name;

    public SubCategory(String name) {
        this();
        this.name = name;
    }

    public SubCategory() {
        this.id = new ObjectId().toHexString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
