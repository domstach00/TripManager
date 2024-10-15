package com.example.tripmanager.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import javax.persistence.GeneratedValue;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@MappedSuperclass
public abstract class AbstractEntity implements Serializable {
    public static final String FIELD_NAME_ID = "id";

    @Id
    @GeneratedValue
    private String id = null;

    public AbstractEntity() {
    }

    public static String toString(ObjectId objectId) {
        return objectId == null
                ? null
                : objectId.toHexString();
    }
    public static ObjectId toObjectId(String str) {
        return str == null || str.isBlank()
                ? null
                : new ObjectId(str);
    }

    public AbstractEntity(String id) {
        this.id = id;
    }

    protected void deepCopyFrom(AbstractEntity that) {
        setId(that.getId());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
