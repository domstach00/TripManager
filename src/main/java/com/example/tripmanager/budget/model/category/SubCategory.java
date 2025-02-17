package com.example.tripmanager.budget.model.category;

import com.example.tripmanager.shared.model.AbstractEntity;
import com.example.tripmanager.shared.repository.AbstractRepositoryImpl;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Field;

public class SubCategory {
    public static final String FIELD_NAME_ID = AbstractRepositoryImpl.FIELD_NAME_ID_WITH_UNDERSCORE;
    public static final String FIELD_NAME_NAME = "name";
    @Field(FIELD_NAME_ID)
    private ObjectId id;
    private String name;

    public SubCategory(String name) {
        this.name = name;
    }

    public SubCategory() {

    }

    public String getId() {
        return AbstractEntity.toString(this.id);
    }

    public void setId(String id) {
        this.id = AbstractEntity.toObjectId(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
