package com.bank.transaction_service.infrastructure.service;

import com.bank.transaction_service.application.service.IdempotencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class IdempotencyServiceImpl  implements IdempotencyService {

    private final ReactiveRedisTemplate<String, String> redisTemplate;
    private final Duration durationDefault = Duration.ofSeconds(30);

    @Override
    public Mono<Boolean> store(String key, String value) {
        return redisTemplate.opsForValue().set(key, value,durationDefault);
    }

    @Override
    public Mono<Boolean> store(String key, String value, Duration timeout) {
        return redisTemplate.opsForValue().set(key, value, timeout);
    }

    @Override
    public Mono<Boolean> hasKey(String key) {
        return redisTemplate.hasKey(key);
    }
}
