package com.bank.transaction_service.infrastructure.service.result;

import com.bank.transaction_service.application.message.TransactionMessage;
import com.bank.transaction_service.application.service.ConsumerHandleFailTransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsumerHandleFailServiceImpl implements ConsumerHandleFailTransactionService {

  @Override
  @RabbitListener(queues = "${james-config-rabbitmq.fail-transaction.queue.queue-fail}")
  public Mono<Void> handleFailTransaction(TransactionMessage transactionMessage) {
    return Mono.just(transactionMessage)
        .flatMap(
            message -> {
              log.info("Notify to user  {} is fail", message);
              return Mono.empty();
            });
  }
}
