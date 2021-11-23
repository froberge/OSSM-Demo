package com.thecat.demos.debitservice.services;

import com.thecat.demos.debitservice.entities.TransactionEntity;
import com.thecat.demos.debitservice.repository.TransactionRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DebitService {

    private final TransactionRepository repository;
    
    public DebitService(TransactionRepository repository) {
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