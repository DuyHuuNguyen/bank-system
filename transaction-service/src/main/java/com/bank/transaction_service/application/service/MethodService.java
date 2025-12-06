package com.bank.transaction_service.application.service;

import com.bank.transaction_service.domain.entity.Method;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MethodService {
  Mono<Method> findById(Long id);

  Mono<Method> save(Method method);

  Mono<Method> findByMethodName(String methodName);

  Flux<Method> findAll(Integer pageSize, Integer offset);
}
