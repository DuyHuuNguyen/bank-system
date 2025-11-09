package com.bank.transaction_service.domain.repository;

import com.bank.transaction_service.domain.entity.Method;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MethodRepository extends R2dbcRepository<Method, Long> {}
