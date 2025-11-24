package com.bank.transaction_service.infrastructure.service;

import com.bank.transaction_service.application.message.TransactionMessage;
import com.bank.transaction_service.application.service.ConsumerHandleRetryTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ConsumerHandleRetryTransactionServiceImpl
    implements ConsumerHandleRetryTransactionService {

  @Override
//  @RabbitListener(queues = "${rabbit-config.queue-retry}")
  public Mono<Void> handleRetryTransaction(TransactionMessage transactionMessage) {
    return null;
  }
}
