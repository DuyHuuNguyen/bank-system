package com.bank.transaction_service.application.service;

import java.time.Duration;
import reactor.core.publisher.Mono;

public interface IdempotencyService {
  Mono<Boolean> hasKey(String key);

  Mono<Boolean> store(String key, String value);

  Mono<Boolean> store(String key, String value, Duration timeout);
}
