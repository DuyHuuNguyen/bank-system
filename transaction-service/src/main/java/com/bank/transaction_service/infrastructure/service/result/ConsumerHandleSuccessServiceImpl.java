package com.bank.transaction_service.infrastructure.service.result;

import com.bank.transaction_service.application.message.TransactionMessage;
import com.bank.transaction_service.application.service.ConsumerHandleSuccessTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ConsumerHandleSuccessServiceImpl implements ConsumerHandleSuccessTransactionService {

  @Override
  //  @RabbitListener(queues = "${rabbit-config.queue-success}")
  public Mono<Void> handleSuccessTransaction(TransactionMessage transactionMessage) {
    return null;
  }
}
