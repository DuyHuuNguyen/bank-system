package com.bank.transaction_service.application.service;

import com.bank.transaction_service.application.dto.TransactionHistoryDTO;
import com.bank.transaction_service.domain.entity.Transaction;
import com.bank.transaction_service.infrastructure.util.TransactionCriteria;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface TransactionService {
  Mono<Transaction> save(Transaction transaction);

  Flux<TransactionHistoryDTO> findAll(String conditions,Integer pageSize,Integer pageNumber);

  Flux<TransactionHistoryDTO> findAll(TransactionCriteria criteria);
}
