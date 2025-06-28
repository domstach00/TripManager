package com.example.tripmanager.shared.model;

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
    public static final String FIELD_NAME_CREATED_TIME = "createdTime";
    public static final String FIELD_NAME_CREATED_BY = "createdBy";
    public static final String FIELD_NAME_LAST_MODIFIED_TIME = "lastModifiedTime";
    public static final String FIELD_NAME_LAST_MODIFIED_BY = "lastModifiedBy";
    public static final String FIELD_NAME_IS_DELETED = "isDeleted";


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

    private boolean isDeleted = false;

    public AbstractAuditable() {
        super();
    }

    protected void deepCopyFrom(AbstractAuditable that) {
        super.deepCopyFrom(that);
    }


    @JsonFormat(shape = JsonFormat.Shape.STRING)
    public Instant getCreatedTime() {
        return createdTime == null
                ? null
                : createdTime.truncatedTo(ChronoUnit.MILLIS);
    }

    public String getCreatedBy() {
        return createdBy;
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

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
