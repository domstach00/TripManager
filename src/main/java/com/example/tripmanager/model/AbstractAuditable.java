package com.example.tripmanager.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.event.AuditingEntityCallback;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@MappedSuperclass
@EntityListeners(AuditingEntityCallback.class)
public abstract class AbstractAuditable extends AbstractEntity {

    @CreatedDate
    @NotNull
    @PastOrPresent
    private Instant createdTime = null;

    @CreatedBy
    @NotEmpty
    private String createdBy = null;

    @LastModifiedDate
    @NotNull
    @PastOrPresent
    private Instant lastModifiedTime = null;

    @LastModifiedBy
    @NotEmpty
    private String lastModifiedBy = null;

    public AbstractAuditable() {
        super();
    }

    protected void deepCopyFrom(AbstractAuditable that) {
        super.deepCopyFrom(that);
        setCreatedTime(that.getCreatedTime());
        setCreatedBy(that.getCreatedBy());
        setLastModifiedTime(that.getLastModifiedTime());
        setLastModifiedBy(that.getLastModifiedBy());
    }


    @JsonFormat(shape = JsonFormat.Shape.STRING)
    public Instant getCreatedTime() {
        return createdTime == null
                ? null
                : createdTime.truncatedTo(ChronoUnit.MILLIS);
    }

    public void setCreatedTime(Instant createdTime) {
        this.createdTime = createdTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    public Instant getLastModifiedTime() {
        return lastModifiedTime == null
                ? null
                : lastModifiedTime.truncatedTo(ChronoUnit.MILLIS);
    }

    public void setLastModifiedTime(Instant lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }
}
