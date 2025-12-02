package com.bank.transaction_service.infrastructure.config;

import com.bank.transaction_service.application.dto.TransactionHistoryDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

@Configuration
public class RedisConfig {

  @Bean
  public ReactiveRedisTemplate<String, Object> reactiveRedisTemplate(
      ReactiveRedisConnectionFactory factory) {
    Jackson2JsonRedisSerializer<Object> serializer =
        new Jackson2JsonRedisSerializer<>(Object.class);

    RedisSerializationContext.RedisSerializationContextBuilder<String, Object> builder =
        RedisSerializationContext.newSerializationContext(serializer);

    RedisSerializationContext<String, Object> context = builder.build();

    return new ReactiveRedisTemplate<>(factory, context);
  }

  @Bean(name = "CacheTransactions")
  public ReactiveRedisTemplate<String, TransactionHistoryDTO>
      CacheTransactionsWithReactiveRedisTemplate(ReactiveRedisConnectionFactory factory) {
    Jackson2JsonRedisSerializer<TransactionHistoryDTO> serializer =
        new Jackson2JsonRedisSerializer<>(TransactionHistoryDTO.class);

    RedisSerializationContext.RedisSerializationContextBuilder<String, TransactionHistoryDTO>
        builder = RedisSerializationContext.newSerializationContext(serializer);

    RedisSerializationContext<String, TransactionHistoryDTO> context = builder.build();

    return new ReactiveRedisTemplate<>(factory, context);
  }
}
