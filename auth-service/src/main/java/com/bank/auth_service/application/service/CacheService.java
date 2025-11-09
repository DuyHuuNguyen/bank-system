package com.bank.auth_service.application.service;

import java.time.Duration;
import reactor.core.publisher.Mono;

public interface CacheService {
  Mono<Boolean> store(String key, String value);

  Mono<Boolean> store(String key, String value, Duration timeOut);
}
