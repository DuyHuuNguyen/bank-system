package com.bank.transaction_service.infrastructure.service;

import com.bank.transaction_service.application.service.MethodService;
import com.bank.transaction_service.domain.entity.Method;
import com.bank.transaction_service.domain.repository.MethodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MethodServiceImpl implements MethodService {
  private final MethodRepository methodRepository;

  @Override
  public Mono<Method> findById(Long id) {
    return this.methodRepository.findById(id);
  }

  @Override
  public Mono<Method> save(Method method) {
    return this.methodRepository.save(method);
  }
}
