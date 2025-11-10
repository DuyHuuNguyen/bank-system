package com.bank.auth_service.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

  //  @Bean(name = "redis-jwt")
  public ReactiveRedisTemplate<String, String> reactiveRedisTemplate(
      ReactiveRedisConnectionFactory factory) {

    StringRedisSerializer stringSerializer = new StringRedisSerializer();

    RedisSerializationContext.RedisSerializationContextBuilder<String, String> builder =
        RedisSerializationContext.newSerializationContext(stringSerializer);

    RedisSerializationContext<String, String> context =
        builder
            .key(stringSerializer)
            .value(stringSerializer)
            .hashKey(stringSerializer)
            .hashValue(stringSerializer)
            .build();
    return new ReactiveRedisTemplate<>(factory, context);
  }
}
