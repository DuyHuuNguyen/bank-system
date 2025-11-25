package com.bank.transaction_service.infrastructure.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQRetryTransactionConfig {
  @Value("${james-config-rabbitmq.retry-transaction.exchange.exchange-retry}")
  private String exchangeName;

  @Value("${james-config-rabbitmq.retry-transaction.queue.retry-transfer-queue}")
  private String transferQueueName;

  @Value("${james-config-rabbitmq.retry-transaction.queue.retry-deposit-queue}")
  private String depositQueueName;

  @Value("${james-config-rabbitmq.retry-transaction.queue.retry-withdraw-queue}")
  private String withdrawQueueName;

  @Value("${james-config-rabbitmq.retry-transaction.routing.retry-transfer-queue}")
  private String transferRoutingKey;

  @Value("${james-config-rabbitmq.retry-transaction.routing.retry-deposit-queue}")
  private String depositRoutingKey;

  @Value("${james-config-rabbitmq.retry-transaction.routing.retry-withdraw-queue}")
  private String withdrawRoutingKey;

  @Bean
  public DirectExchange retryExchange() {
    return new DirectExchange(exchangeName);
  }

  @Bean
  public Queue retryTransferQueue() {
    return new Queue(transferQueueName);
  }

  @Bean
  public Queue retryDepositQueue() {
    return new Queue(depositQueueName);
  }

  @Bean
  public Queue retryWithdrawQueue() {
    return new Queue(withdrawQueueName);
  }

  @Bean
  public Binding retryTransferBinding(Queue retryTransferQueue, DirectExchange retryExchange) {
    return BindingBuilder.bind(retryTransferQueue).to(retryExchange).with(transferRoutingKey);
  }

  @Bean
  public Binding retryDepositBinding(Queue retryDepositQueue, DirectExchange retryExchange) {
    return BindingBuilder.bind(retryDepositQueue).to(retryExchange).with(depositRoutingKey);
  }

  @Bean
  public Binding retryWithdrawBinding(Queue retryWithdrawQueue, DirectExchange retryExchange) {
    return BindingBuilder.bind(retryWithdrawQueue).to(retryExchange).with(withdrawRoutingKey);
  }

  @Bean
  public MessageConverter jsonMessageConverter() {
    return new Jackson2JsonMessageConverter();
  }
}
