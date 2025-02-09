package com.example.tripmanager.budget.repository;

import com.example.tripmanager.budget.model.Transaction;
import com.example.tripmanager.shared.repository.AbstractRepositoryImpl;
import org.springframework.stereotype.Repository;

@Repository
public class TransactionRepository extends AbstractRepositoryImpl<Transaction> {
    @Override
    protected Class<Transaction> getEntityClass() {
        return Transaction.class;
    }


}
