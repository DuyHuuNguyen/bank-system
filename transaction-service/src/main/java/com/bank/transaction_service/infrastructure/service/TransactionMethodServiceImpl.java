package com.bank.transaction_service.infrastructure.service;

import com.bank.transaction_service.application.service.TransactionMethodService;
import com.bank.transaction_service.domain.entity.TransactionMethod;
import com.bank.transaction_service.domain.repository.TransactionMethodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TransactionMethodServiceImpl implements TransactionMethodService {
  private final TransactionMethodRepository transactionMethodRepository;

  @Override
  public Mono<TransactionMethod> findById(Long id) {
    return this.transactionMethodRepository.findById(id);
  }

  @Override
  public Mono<TransactionMethod> save(TransactionMethod transactionMethod) {
    return this.transactionMethodRepository.save(transactionMethod);
  }
}
