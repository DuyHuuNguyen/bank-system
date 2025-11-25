package com.bank.transaction_service.application.service;

import com.bank.transaction_service.domain.entity.Transaction;
import reactor.core.publisher.Mono;

public interface TransactionService {
  Mono<Transaction> save(Transaction transaction);
}
