package com.bank.transaction_service.infrastructure.service;

import com.bank.transaction_service.application.message.TransactionMessage;
import com.bank.transaction_service.application.service.ProducerFailTransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProducerFailTransactionServiceImpl implements ProducerFailTransactionService {

  private final RabbitTemplate rabbitTemplate;

  @Value("${james-config-rabbitmq.fail-transaction.exchange.exchange-fail}")
  private String failTransactionExchange;

  @Value("${james-config-rabbitmq.fail-transaction.routing.key-fail}")
  private String failTransactionRoutingKey;

  @Override
  public Mono<Void> sendFailTransactionMessage(TransactionMessage transactionMessage) {
    log.info("Send fail transaction message");
    return Mono.fromRunnable(
        () ->
            this.rabbitTemplate.convertAndSend(
                failTransactionExchange, failTransactionRoutingKey, transactionMessage));
  }
}
