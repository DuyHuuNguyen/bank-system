package com.bank.wallet_service.infrastructure.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
  @Value("${rabbit-MQ.queue-default-wallet}")
  private String defaultWalletQueue;

  @Value("${rabbit-MQ.exchange-default-wallet}")
  private String defaultWalletExchange;

  @Value("${rabbit-MQ.routing-key-default-wallet}")
  private String defaultWalletRoutingKey;

  @Bean
  public Queue defaultWalletQueue() {
    return new Queue(this.defaultWalletQueue, true);
  }

  @Bean
  public DirectExchange defaultWalletExchange() {
    return new DirectExchange(this.defaultWalletExchange, true, false);
  }

  @Bean
  public Binding binding() {
    return BindingBuilder.bind(this.defaultWalletQueue())
        .to(defaultWalletExchange())
        .with(this.defaultWalletRoutingKey);
  }
}
