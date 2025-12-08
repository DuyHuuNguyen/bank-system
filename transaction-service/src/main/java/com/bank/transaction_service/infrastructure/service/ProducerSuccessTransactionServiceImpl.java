package com.bank.transaction_service.infrastructure.service;

import com.bank.transaction_service.application.message.TransactionMessage;
import com.bank.transaction_service.application.service.ProducerSuccessTransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProducerSuccessTransactionServiceImpl implements ProducerSuccessTransactionService {

  private final RabbitTemplate rabbitTemplate;

  @Value("${james-config-rabbitmq.done-transaction.exchange.exchange-success}")
  private String successTransactionExchange;

  @Value("${james-config-rabbitmq.done-transaction.routing.key-success}")
  private String successTransactionRoutingKey;

  @Override
  public Mono<Boolean> sendSuccessTransactionMessage(TransactionMessage transactionMessage) {
    log.info("Send successful transaction Message");
    return Mono.fromRunnable(
        () ->
            this.rabbitTemplate.convertAndSend(
                successTransactionExchange, successTransactionRoutingKey, transactionMessage));
  }
}
