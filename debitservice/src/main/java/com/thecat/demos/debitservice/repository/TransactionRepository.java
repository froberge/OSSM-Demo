package com.thecat.demos.debitservice.repository;

import java.util.List;

import com.thecat.demos.debitservice.entities.TransactionEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Integer>{

    public List<TransactionEntity> findTransactionByClientId(String clientId);
}
