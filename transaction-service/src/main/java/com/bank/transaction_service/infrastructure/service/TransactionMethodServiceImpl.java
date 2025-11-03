package com.bank.transaction_service.infrastructure.service;

import com.bank.transaction_service.application.service.TransactionMethodService;
import com.bank.transaction_service.domain.repository.TransactionMethodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionMethodServiceImpl implements TransactionMethodService {
    private final TransactionMethodRepository transactionMethodRepository;
}
