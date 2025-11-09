package com.bank.transaction_service.infrastructure.service;

import com.bank.transaction_service.application.service.TransactionFeeService;
import com.bank.transaction_service.domain.repository.TransactionFeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionFeeServiceImpl implements TransactionFeeService {
  private final TransactionFeeRepository transactionFeeRepository;
}
