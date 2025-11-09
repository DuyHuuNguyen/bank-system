package com.bank.transaction_service.domain.repository;

import com.bank.transaction_service.domain.entity.TransactionMethod;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionMethodRepository extends R2dbcRepository<TransactionMethod, Long> {}
