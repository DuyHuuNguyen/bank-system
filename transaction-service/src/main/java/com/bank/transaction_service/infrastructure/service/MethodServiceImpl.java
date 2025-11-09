package com.bank.transaction_service.infrastructure.service;

import com.bank.transaction_service.application.service.MethodService;
import com.bank.transaction_service.domain.repository.MethodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MethodServiceImpl implements MethodService {
  private final MethodRepository methodRepository;
}
