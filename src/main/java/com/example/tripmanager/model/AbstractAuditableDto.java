package com.example.tripmanager.model;

import com.example.tripmanager.model.account.AccountDto;

import java.time.Instant;

public abstract class AbstractAuditableDto extends AbstractEntityDto {
    private String createdTime;
    private AccountDto createdBy;
    private String lastModifiedTime;
    private AccountDto lastModifiedBy;

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Instant createdTime) {
        this.createdTime = createdTime.toString();
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
        this.lastModifiedTime = lastModifiedTime.toString();
    }

    public AccountDto getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(AccountDto lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }
}
