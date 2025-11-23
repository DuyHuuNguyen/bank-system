package com.bank.user_service.infrastructure.service;

import com.bank.user_service.application.messsage.CreateAccountMessage;
import com.bank.user_service.application.service.ProducerCreateAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
public class ProducerCreateAccountServiceImpl implements ProducerCreateAccountService {

  private final RabbitTemplate rabbitTemplate;

  @Value("${rabbitmqs.exchange.create-account}")
  private String createAccountDirectExchange;

  @Value("${rabbitmqs.routing-key.create-account}")
  private String createAccountRoutingKey;

  @Override
  public Mono<Void> sendCreateAccountMessage(CreateAccountMessage createAccountMessage) {
    return Mono.fromRunnable(
            () ->
                rabbitTemplate.convertAndSend(
                    this.createAccountDirectExchange,
                    this.createAccountRoutingKey,
                    createAccountMessage))
        .subscribeOn(Schedulers.boundedElastic())
        .then();
  }
}
