package com.bank.transaction_service.infrastructure.config;

import java.util.HashMap;
import java.util.Map;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQResultTransactionConfig {

  @Value("${james-config-rabbitmq.fail-transaction.queue.queue-fail}")
  private String FAIL_QUEUE;

  @Value("${james-config-rabbitmq.done-transaction.queue.queue-success}")
  private String SUCCESS_QUEUE;

  @Value("${james-config-rabbitmq.fail-transaction.exchange.exchange-fail}")
  private String FAIL_EXCHANGE;

  @Value("${james-config-rabbitmq.done-transaction.exchange.exchange-success}")
  private String SUCCESS_EXCHANGE;

  @Value("${james-config-rabbitmq.fail-transaction.routing.key-fail}")
  private String FAIL_KEY;

  @Value("${james-config-rabbitmq.done-transaction.routing.key-success}")
  private String SUCCESS_KEY;

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
  public DirectExchange transactionFailExchange() {
    return new DirectExchange(FAIL_EXCHANGE);
  }

  @Bean
  public DirectExchange transactionSuccessExchange() {
    return new DirectExchange(SUCCESS_EXCHANGE);
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
