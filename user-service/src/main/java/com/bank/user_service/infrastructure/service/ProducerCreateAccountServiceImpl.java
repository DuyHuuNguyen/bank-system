package com.bank.user_service.infrastructure.service;

import com.bank.user_service.application.messsage.CreateAccountMessage;
import com.bank.user_service.application.service.ProducerCreateAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
public class ProducerCreateAccountServiceImpl implements ProducerCreateAccountService {

  private final RabbitTemplate rabbitTemplate;

  private String createAccountDirectExchange;

  private String createAccountRoutingKey;

  @Override
  public Mono<Void> onCreateAccountMessage(CreateAccountMessage createAccountMessage) {
    return Mono.fromRunnable(
            () ->
                rabbitTemplate.convertAndSend(
                    "23130075-create-account-exchange",
                    "23130075-create-account-routing-key",
                    createAccountMessage))
        .subscribeOn(Schedulers.boundedElastic())
        .then();
  }
}
