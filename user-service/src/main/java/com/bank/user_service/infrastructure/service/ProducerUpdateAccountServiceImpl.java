package com.bank.user_service.infrastructure.service;

import com.bank.user_service.application.messsage.UpdateAccountMessage;
import com.bank.user_service.application.service.ProducerUpdateAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
public class ProducerUpdateAccountServiceImpl implements ProducerUpdateAccountService {

  private final RabbitTemplate rabbitTemplate;

  @Override
  public Mono<Void> sendMessageUpdateAccount(UpdateAccountMessage updateAccountMessage) {
    return Mono.fromRunnable(
            () ->
                rabbitTemplate.convertAndSend(
                    "23130075-change-account-exchange23130075-direct-exchange",
                    "23130075-routing-key",
                    updateAccountMessage))
        .subscribeOn(Schedulers.boundedElastic())
        .then();
  }
}
