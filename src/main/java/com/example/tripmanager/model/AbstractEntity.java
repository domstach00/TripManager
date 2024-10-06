package com.example.tripmanager.model;

import org.springframework.data.annotation.Id;

import javax.persistence.GeneratedValue;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@MappedSuperclass
public abstract class AbstractEntity implements Serializable {
    @Id
    @GeneratedValue
    private String id = null;

    public AbstractEntity() {
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
