package com.bank.transaction_service.infrastructure.config;

import java.util.HashMap;
import java.util.Map;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

  public static final String HANDLE_QUEUE = "23130076-transaction-handle-queue";
  public static final String RETRY_QUEUE = "23130076-transaction-retry-queue";
  public static final String FAIL_QUEUE = "23130076-transaction-fail-queue";
  public static final String SUCCESS_QUEUE = "23130076-transaction-success-queue";

  public static final String HANDLE_EXCHANGE = "23130076-transaction-handle-exchange";
  public static final String RETRY_EXCHANGE = "23130076-transaction-retry-exchange";
  public static final String FAIL_EXCHANGE = "23130076-transaction-fail-exchange";
  public static final String SUCCESS_EXCHANGE = "23130076-transaction-success-exchange";

  public static final String HANDLE_KEY = "23130076.transaction.handle";
  public static final String RETRY_KEY = "23130076.transaction.retry";
  public static final String FAIL_KEY = "23130076.transaction.fail";
  public static final String SUCCESS_KEY = "23130076.transaction.success";

  @Bean
  public Queue transactionHandleQueue() {
    boolean isDurable = true;
    boolean isExclusive = false;
    boolean isAutoDelete = false;
    Map<String, Object> arguments = new HashMap<String, Object>();
    arguments.put("x-messages-ttl", 60000);
    arguments.put("x-expires", 5 * 60 * 1000);
    System.out.println(HANDLE_QUEUE);
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

  @Bean
  public Binding transactionSuccessBinding() {
    return BindingBuilder.bind(this.transactionSuccessExchange())
        .to(this.transactionSuccessExchange())
        .with(SUCCESS_KEY);
  }
}
