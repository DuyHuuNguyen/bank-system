package com.bank.auth_service.infrastructure.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
  @Value("${rabbitmq.queue.update-info-account}")
  private String updateInfoAccountQueue;

  @Value("${rabbitmq.exchange.update-info-account}")
  private String updateInfoAccountExchange;

  @Value("${rabbitmq.routing-key.update-info-account}")
  private String updateInfoAccountRoutingKey;

  @Bean
  public Queue updateInfoAccountQueue() {
    return new Queue(this.updateInfoAccountQueue);
  }

  @Bean
  public DirectExchange updateInfoAccountExchange() {
    return new DirectExchange(this.updateInfoAccountExchange);
  }

  @Bean
  public Binding updateInfoAccountBinding() {
    return BindingBuilder.bind(this.updateInfoAccountQueue())
        .to(this.updateInfoAccountExchange())
        .with(this.updateInfoAccountRoutingKey);
  }
}
