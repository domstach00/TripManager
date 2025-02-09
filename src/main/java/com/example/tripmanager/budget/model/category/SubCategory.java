package com.example.tripmanager.budget.model.category;

import com.example.tripmanager.shared.model.AbstractAuditable;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = SubCategory.COLLECTION_NAME)
public class SubCategory extends AbstractAuditable {
    public static final String COLLECTION_NAME = "SubCategories";
    public static final String FIELD_NAME_NAME = "name";
    private String name;

    public SubCategory(String name) {
        this.name = name;
    }

    public SubCategory() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
