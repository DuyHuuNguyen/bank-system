package com.bank.auth_service.infrastructure.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
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

  @Value("${rabbitmq.queue.create-account}")
  private String createAccountQueue;

  @Value("${rabbitmq.exchange.create-account}")
  private String createAccountExchange;

  @Value("${rabbitmq.routing-key.create-account}")
  private String createAccountRoutingKey;

  @Bean
  public Queue createAccountQueue() {
    return QueueBuilder.durable(this.createAccountQueue).build();
  }

  @Bean
  public DirectExchange createAccountExchange() {
    return new DirectExchange(createAccountExchange);
  }

  @Bean
  public Binding createAccountBinding() {
    return BindingBuilder.bind(this.createAccountQueue())
        .to(this.createAccountExchange())
        .with(this.createAccountRoutingKey);
  }

  @Bean
  public MessageConverter jsonMessageConverter() {
    return new Jackson2JsonMessageConverter();
  }
}
