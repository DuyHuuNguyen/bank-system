package com.bank.transaction_service.infrastructure.service;

import com.bank.transaction_service.application.service.TransactionService;
import com.bank.transaction_service.domain.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
  private final TransactionRepository transactionRepository;
}
