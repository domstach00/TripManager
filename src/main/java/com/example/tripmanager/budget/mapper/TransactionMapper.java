package com.example.tripmanager.budget.mapper;

import com.example.tripmanager.account.service.AccountService;
import com.example.tripmanager.budget.model.Transaction;
import com.example.tripmanager.budget.model.TransactionCreateForm;
import com.example.tripmanager.budget.model.TransactionDto;
import com.example.tripmanager.shared.mapper.AuditableMapper;
import com.example.tripmanager.shared.model.AbstractEntity;
import com.example.tripmanager.shared.util.DateUtils;

import static com.example.tripmanager.shared.model.AbstractEntity.toObjectId;

public class TransactionMapper {

    public static Transaction transactionFromCreateForm(TransactionCreateForm createForm) {
        Transaction transaction = new Transaction();
        transaction.setBudgetId(toObjectId(createForm.getBudgetId()));
        transaction.setCategoryId(toObjectId(createForm.getCategoryId()));
        transaction.setSubCategoryId(toObjectId(createForm.getSubCategoryId()));
        transaction.setDescription(createForm.getDescription());
        transaction.setAmount(createForm.getAmount());
        return transaction;
    }

    public static TransactionDto toDto(Transaction transaction, AccountService accountService) {
        if (transaction == null) {
            return null;
        }
        TransactionDto transactionDto = new TransactionDto();
        AuditableMapper.toDto(transaction, transactionDto, accountService);
        transactionDto.setBudgetId(AbstractEntity.toString(transaction.getBudgetId()));
        transactionDto.setCategoryId(AbstractEntity.toString(transaction.getCategoryId()));
        transactionDto.setSubCategoryId(AbstractEntity.toString(transaction.getSubCategoryId()));
        transactionDto.setDescription(transaction.getDescription());
        transactionDto.setAmount(transaction.getAmount());
        transactionDto.setTransactionDate(DateUtils.formatInstantToDateTimeString(transaction.getTransactionDate()));
        return transactionDto;
    }
}
