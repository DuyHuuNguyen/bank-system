package com.bank.transaction_service.infrastructure.service;

import com.bank.transaction_service.application.service.FeeService;
import com.bank.transaction_service.domain.repository.FeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeeServiceImpl implements FeeService {
  private final FeeRepository feeRepository;
}
