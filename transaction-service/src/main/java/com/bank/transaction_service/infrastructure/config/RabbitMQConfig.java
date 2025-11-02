package com.bank.transaction_service.infrastructure.config;

import java.util.HashMap;
import java.util.Map;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

  @Value("${rabbitmq-config-transaction.transaction-common.queue.transaction-handle-queue}")
  public static String HANDLE_QUEUE;

  @Value("${rabbitmq-config-transaction.transaction-common.queue.transaction-retry-queue}")
  public static String RETRY_QUEUE;

  @Value("${rabbitmq-config-transaction.transaction-common.queue.transaction-fail-queue}")
  public static String FAIL_QUEUE;

  @Value("${rabbitmq-config-transaction.transaction-common.queue.transaction-success-queue}")
  public static String SUCCESS_QUEUE;

  @Value("${rabbitmq-config-transaction.transaction-common.exchange.transaction-handle-exchange}")
  public static String HANDLE_EXCHANGE;

  @Value("${rabbitmq-config-transaction.transaction-common.exchange.transaction-retry-exchange}")
  public static String RETRY_EXCHANGE;

  @Value("${rabbitmq-config-transaction.transaction-common.exchange.transaction-fail-exchange}")
  public static String FAIL_EXCHANGE;

  @Value("${rabbitmq-config-transaction.transaction-common.exchange.transaction-success-exchange}")
  public static String SUCCESS_EXCHANGE;

  @Value("${rabbitmq-config-transaction.transaction-common.routing-key.transaction-handle-key}")
  public static String HANDLE_KEY;

  @Value("${rabbitmq-config-transaction.transaction-common.routing-key.transaction-retry-key}")
  public static String RETRY_KEY;

  @Value("${rabbitmq-config-transaction.transaction-common.routing-key.transaction-fail-key}")
  public static String FAIL_KEY;

  @Value("${rabbitmq-config-transaction.transaction-common.routing-key.transaction-success-key}")
  public static String SUCCESS_KEY;

  @Bean
  public Queue transactionHandleQueue() {
    boolean isDurable = true;
    boolean isExclusive = false;
    boolean isAutoDelete = false;
    Map<String, Object> arguments = new HashMap<String, Object>();
    arguments.put("x-messages-ttl", 60000);
    arguments.put("x-expires", 5 * 60 * 1000);
    return new Queue(HANDLE_QUEUE, isDurable, isExclusive, isAutoDelete, arguments);
  }

  @Bean
  public Queue transactionRetryQueue() {
    Map<String, Object> arguments = new HashMap<String, Object>();
    arguments.put("x-messages-ttl", 60000);
    arguments.put("x-expires", 5 * 60 * 1000);
    return new Queue(RETRY_QUEUE, true, false, false, arguments);
  }

  @Bean
  public Queue transactionFailQueue() {
    Map<String, Object> arguments = new HashMap<String, Object>();
    arguments.put("x-messages-ttl", 60000);
    arguments.put("x-expires", 5 * 60 * 1000);
    return new Queue(FAIL_QUEUE, true, false, false, arguments);
  }

  @Bean
  public Queue transactionSuccessQueue() {
    Map<String, Object> arguments = new HashMap<String, Object>();
    arguments.put("x-messages-ttl", 60000);
    arguments.put("x-expires", 5 * 60 * 1000);
    return new Queue(SUCCESS_QUEUE, true, false, false, arguments);
  }

  @Bean
  public DirectExchange transactionHandleExchange() {
    return new DirectExchange(HANDLE_EXCHANGE);
  }

  @Bean
  public DirectExchange transactionRetryExchange() {
    return new DirectExchange(RETRY_EXCHANGE);
  }

  @Bean
  public DirectExchange transactionFailExchange() {
    return new DirectExchange(FAIL_EXCHANGE);
  }

  @Bean
  public DirectExchange transactionSuccessExchange() {
    return new DirectExchange(SUCCESS_EXCHANGE);
  }

  @Bean
  public Binding transactionHandleBinding() {
    return BindingBuilder.bind(this.transactionHandleQueue())
        .to(this.transactionHandleExchange())
        .with(HANDLE_KEY);
  }

  @Bean
  public Binding transactionRetryBinding() {
    return BindingBuilder.bind(this.transactionRetryQueue())
        .to(this.transactionRetryExchange())
        .with(RETRY_KEY);
  }

  @Bean
  public Binding transactionFailBinding() {
    return BindingBuilder.bind(this.transactionFailQueue())
        .to(this.transactionFailExchange())
        .with(FAIL_KEY);
  }
}
