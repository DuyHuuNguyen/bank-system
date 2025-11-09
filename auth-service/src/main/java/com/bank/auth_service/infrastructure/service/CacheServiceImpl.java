package com.bank.auth_service.infrastructure.service;

import com.bank.auth_service.application.service.CacheService;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CacheServiceImpl implements CacheService {

  @Qualifier("redis-jwt")
  private final ReactiveRedisTemplate<String, String> redisTemplate;

  @Override
  public Mono<Boolean> store(String key, String value) {
    return redisTemplate.opsForValue().set(key, value);
  }

  @Override
  public Mono<Boolean> store(String key, String value, Duration timeout) {
    return redisTemplate.opsForValue().set(key, value, timeout);
  }
}
