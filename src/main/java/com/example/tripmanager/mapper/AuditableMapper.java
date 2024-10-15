package com.example.tripmanager.mapper;

import com.example.tripmanager.exception.AccountNotFoundException;
import com.example.tripmanager.model.AbstractAuditable;
import com.example.tripmanager.model.AbstractAuditableDto;
import com.example.tripmanager.model.account.Account;
import com.example.tripmanager.service.AccountService;

import java.util.Objects;

public class AuditableMapper {
    public static <T extends AbstractAuditable, DTO extends AbstractAuditableDto> DTO toDto(T auditable, DTO auditableDto, AccountService accountService) {
        if (auditable == null || auditableDto == null) {
            return null;
        }
        auditableDto.setId(auditable.getId());
        auditableDto.setCreatedTime(auditable.getCreatedTime());
        auditableDto.setLastModifiedTime(auditable.getLastModifiedTime());
        auditableDto.setDeleted(auditable.isDeleted());

        if (accountService != null) {
            final Account createdBy = accountService.getAccountById(auditable.getCreatedBy()).orElseThrow(AccountNotFoundException::new);
            auditableDto.setCreatedBy(AccountMapper.toDto(createdBy));

            final Account lastModifiedBy = Objects.equals(createdBy.getId(), auditable.getLastModifiedBy())
                    ? createdBy
                    : accountService.getAccountById(auditable.getLastModifiedBy()).orElseThrow(AccountNotFoundException::new);
            auditableDto.setLastModifiedBy(AccountMapper.toDto(lastModifiedBy));
        }
        return auditableDto;
    }
}
