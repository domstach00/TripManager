package com.example.tripmanager.shared.model;

import com.example.tripmanager.account.model.AccountDto;

import java.time.Instant;

public abstract class AbstractAuditableDto extends AbstractEntityDto {
    private String createdTime;
    private AccountDto createdBy;
    private String lastModifiedTime;
    private AccountDto lastModifiedBy;
    private Boolean isDeleted;

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Instant createdTime) {
        this.createdTime = createdTime == null ? null : createdTime.toString();
    }

    public AccountDto getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(AccountDto createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(Instant lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime == null ? null : lastModifiedTime.toString();
    }

    public AccountDto getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(AccountDto lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}
