package com.bank.transaction_service.application.service;

import com.bank.transaction_service.domain.entity.TransactionMethod;
import reactor.core.publisher.Mono;

public interface TransactionMethodService {
  Mono<TransactionMethod> findById(Long id);

  Mono<TransactionMethod> save(TransactionMethod transactionMethod);
}
