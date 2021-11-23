package com.thecat.demos.creditservice.services;

import com.thecat.demos.creditservice.entities.TransactionEntity;
import com.thecat.demos.creditservice.repository.TransactionRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreditService {

    private final TransactionRepository repository;
    
    public CreditService(TransactionRepository repository) {
        this.repository = repository;
    }

    /**
     * Add a new transaction in the table.
     */
    public TransactionEntity addTransaction(TransactionEntity entity ) {
        return repository.save(entity);
    }

    /**
     * Retrieve all the Transactions made by a given client.
     * 
     * @param clientId
     * @return List<{@link TransactionEntity}>
     */
    public List<TransactionEntity> findTransactionByClientId(String clientId) {
        return repository.findTransactionByClientId( clientId );
    }

    /**
     * Retrieve all the transaction in the table
     * 
     * @return List<{@link TransactionEntity}>
     */
    public List<TransactionEntity> findAllTransactions() {
        return repository.findAll();
    }
}