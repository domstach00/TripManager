package com.example.tripmanager.budget.mapper;

import com.example.tripmanager.account.mapper.AccountMapper;
import com.example.tripmanager.account.model.Account;
import com.example.tripmanager.account.service.AccountService;
import com.example.tripmanager.budget.model.Budget;
import com.example.tripmanager.budget.model.BudgetCreateForm;
import com.example.tripmanager.budget.model.BudgetDto;
import com.example.tripmanager.budget.model.BudgetTemplate;
import com.example.tripmanager.shared.mapper.AuditableMapper;
import com.example.tripmanager.shared.model.AbstractEntity;
import com.example.tripmanager.shared.util.DateUtils;
import org.bson.types.ObjectId;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Optional;
import java.util.stream.Collectors;

public class BudgetMapper {

    public static Budget budgetFromTemplate(BudgetTemplate budgetTemplate) {
        Budget budget = new Budget();
        budget.setOwnerId(budgetTemplate.getOwnerId());
        budget.setTemplateId(new ObjectId(budgetTemplate.getId()));
        budget.setName(budgetTemplate.getName());
        budget.setCategories(budgetTemplate.getCategories());
        budget.setDescription(budgetTemplate.getDescription());
        budget.setAllocatedBudget(budgetTemplate.getAllocatedBudget());
        budget.setArchived(false);

        budget.setStartDate(Instant.now());
        BudgetTemplate.BudgetPeriod budgetPeriod = budgetTemplate.getBudgetPeriod();
        if (budgetPeriod != null) {
            Instant endDate = budgetPeriod.calculateEndDate(budget.getStartDate());
            budget.setEndDate(endDate);
        }
        return budget;
    }

    public static Budget budgetFromCreateForm(BudgetCreateForm createForm, Account currentAccount) {
        Budget budget = new Budget();
        if (currentAccount != null) {
            budget.setOwnerId(new ObjectId(currentAccount.getId()));
        }
        budget.setName(createForm.getName());
        budget.setDescription(createForm.getDescription());
        budget.setAllocatedBudget(createForm.getAllocatedBudget());
        if (createForm.getStartDate() != null) {
            budget.setStartDate(createForm.getStartDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
        if (createForm.getEndDate() != null) {
            budget.setEndDate(createForm.getEndDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
        return budget;
    }

    public static BudgetDto toDto(Budget budget, AccountService accountService) {
        if (budget == null) {
            return null;
        }
        BudgetDto budgetDto = new BudgetDto();
        AuditableMapper.toDto(budget, budgetDto, accountService);
        Optional<Account> ownerOpt = accountService.getAccountById(budget.getOwnerId().toHexString());
        ownerOpt.ifPresent(owner -> budgetDto.setOwnerId(AccountMapper.toDto(owner)));
        budgetDto.setMembers(budget.getMembers().stream().map(AbstractEntity::toString).collect(Collectors.toList()));
        budgetDto.setTemplateId(AbstractEntity.toString(budget.getTemplateId()));
        budgetDto.setName(budget.getName());
        budgetDto.setCategories(budget.getCategories());
        budgetDto.setDescription(budget.getDescription());
        budgetDto.setAllocatedBudget(budget.getAllocatedBudget());
        budgetDto.setStartDate(DateUtils.formatInstantToDateString(budget.getStartDate()));
        budgetDto.setEndDate(DateUtils.formatInstantToDateString(budget.getEndDate()));
        budgetDto.setArchived(budget.isArchived());
        return budgetDto;
    }
}
