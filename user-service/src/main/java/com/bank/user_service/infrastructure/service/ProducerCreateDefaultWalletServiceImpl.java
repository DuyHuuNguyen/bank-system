package com.bank.user_service.infrastructure.service;

import com.bank.user_service.application.messsage.DefaultWalletMessage;
import com.bank.user_service.application.service.ProducerCreateDefaultWalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
public class ProducerCreateDefaultWalletServiceImpl implements ProducerCreateDefaultWalletService {

  private RabbitTemplate rabbitTemplate;

  @Value("${rabbitmqs.exchange.exchange-default-wallet}")
  private String exchange;

  @Value("${rabbitmqs.routing-key.routing-key-default-wallet}")
  private String routingKey;

  @Override
  public Mono<Void> sendMessageCreateDefaultWallet(DefaultWalletMessage message) {
    return Mono.fromRunnable(
            () -> rabbitTemplate.convertAndSend(this.exchange, this.routingKey, message))
        .subscribeOn(Schedulers.boundedElastic())
        .then();
  }
}
