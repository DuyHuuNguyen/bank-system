package com.bank.transaction_service.infrastructure.service;

import com.bank.transaction_service.application.message.TransactionMessage;
import com.bank.transaction_service.application.service.ConsumerHandleTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ConsumerHandleTransactionServiceImpl implements ConsumerHandleTransactionService {

  @Override
  //  @RabbitListener(queues = "${rabbit-config.queue-handle}")
  public Mono<Void> handleTransaction(TransactionMessage transactionMessage) {
    return null;
  }
}
