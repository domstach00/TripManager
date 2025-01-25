package com.example.tripmanager.budget.mapper;

import com.example.tripmanager.account.mapper.AccountMapper;
import com.example.tripmanager.account.model.Account;
import com.example.tripmanager.account.model.AccountDto;
import com.example.tripmanager.account.service.AccountService;
import com.example.tripmanager.budget.model.BudgetTemplate;
import com.example.tripmanager.budget.model.BudgetTemplateDto;
import com.example.tripmanager.shared.mapper.AuditableMapper;
import org.bson.types.ObjectId;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class BudgetTemplateMapper {

    public static BudgetTemplateDto toDto(BudgetTemplate budgetTemplate, AccountService accountService) {
        if (budgetTemplate == null) {
            return null;
        }
        BudgetTemplateDto budgetTemplateDto = new BudgetTemplateDto();
        budgetTemplateDto = AuditableMapper.toDto(budgetTemplate, budgetTemplateDto, accountService);
        budgetTemplateDto.setPublic(budgetTemplate.isPublic());
        budgetTemplateDto.setName(budgetTemplate.getName());
        budgetTemplateDto.setDescription(budgetTemplate.getDescription());
        budgetTemplateDto.setCategories(budgetTemplate.getCategories());
        budgetTemplateDto.setAllocatedBudget(budgetTemplate.getAllocatedBudget());
        budgetTemplateDto.setArchived(budgetTemplate.isArchived());

        Optional<Account> ownerOpt = accountService.getAccountById(budgetTemplate.getId());
        if (ownerOpt.isPresent()) {
            AccountDto ownerDto = AccountMapper.toDto(ownerOpt.get());
            budgetTemplateDto.setOwnerId(ownerDto);
        }

        return budgetTemplateDto;
    }

    public static BudgetTemplate createFromDto(BudgetTemplateDto budgetTemplateDto) {
        BudgetTemplate budgetTemplate = new BudgetTemplate();
        budgetTemplate.setId(budgetTemplateDto.getId());
        budgetTemplate.setOwnerId(new ObjectId(budgetTemplateDto.getOwnerId().getId()));
        budgetTemplate.setPublic(budgetTemplateDto.isPublic());
        budgetTemplate.setName(budgetTemplateDto.getName());
        budgetTemplate.setDescription(budgetTemplateDto.getDescription());
        budgetTemplate.setCategories(budgetTemplateDto.getCategories().stream().filter(Objects::nonNull).collect(Collectors.toList()));
        budgetTemplate.setAllocatedBudget(budgetTemplateDto.getAllocatedBudget());
        budgetTemplate.setArchived(budgetTemplateDto.isArchived());
        return budgetTemplate;
    }
}
