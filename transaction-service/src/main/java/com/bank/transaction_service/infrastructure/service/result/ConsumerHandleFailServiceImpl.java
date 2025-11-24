package com.bank.transaction_service.infrastructure.service.result;

import com.bank.transaction_service.application.message.TransactionMessage;
import com.bank.transaction_service.application.service.ConsumerHandleFailTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ConsumerHandleFailServiceImpl implements ConsumerHandleFailTransactionService {

  @Override
  //  @RabbitListener(queues = "${rabbit-config.queue-fail}")
  public Mono<Void> handleFailTransaction(TransactionMessage transactionMessage) {
    return null;
  }
}
