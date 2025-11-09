package com.bank.transaction_service.domain.repository;

import com.bank.transaction_service.domain.entity.Transaction;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends R2dbcRepository<Transaction, Long> {}
