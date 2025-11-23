package com.bank.transaction_service.infrastructure.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQStartHandleTransactionConfig {

  @Value("${james-config-rabbitmq.start-transaction.exchange.exchange}")
  private String exchangeName;

  @Value("${james-config-rabbitmq.start-transaction.queue.transfer-queue}")
  private String transferQueue;

  @Value("${james-config-rabbitmq.start-transaction.queue.deposit-queue}")
  private String depositQueue;

  @Value("${james-config-rabbitmq.start-transaction.queue.withdraw-queue}")
  private String withDrawQueue;

  @Value("${james-config-rabbitmq.start-transaction.routing.transfer-queue}")
  private String routingForTransfer;

  @Value("${james-config-rabbitmq.start-transaction.routing.deposit-queue}")
  private String routingForDeposit;

  @Value("${james-config-rabbitmq.start-transaction.routing.withdraw-queue}")
  private String routingForWithdraw;

  @Bean
  public DirectExchange paymentExchange() {
    return new DirectExchange(this.exchangeName);
  }

  @Bean
  public Queue transferQueue() {
    return new Queue(this.transferQueue);
  }

  @Bean
  public Queue depositQueue() {
    return new Queue(this.depositQueue);
  }

  @Bean
  public Queue withdrawQueue() {
    return new Queue(this.withDrawQueue);
  }

  @Bean
  public Binding transferBinding(Queue transferQueue, TopicExchange paymentExchange) {
    return BindingBuilder.bind(transferQueue).to(paymentExchange).with(this.routingForTransfer);
  }

  @Bean
  public Binding depositBinding(Queue depositQueue, TopicExchange paymentExchange) {
    return BindingBuilder.bind(depositQueue).to(paymentExchange).with(this.routingForDeposit);
  }

  @Bean
  public Binding withdrawBinding(Queue withdrawQueue, TopicExchange paymentExchange) {
    return BindingBuilder.bind(withdrawQueue).to(paymentExchange).with(this.routingForWithdraw);
  }
}
