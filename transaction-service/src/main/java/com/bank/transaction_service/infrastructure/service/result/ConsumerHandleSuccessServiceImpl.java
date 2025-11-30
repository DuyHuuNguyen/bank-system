package com.bank.transaction_service.infrastructure.service.result;

import com.bank.transaction_service.application.message.TransactionMessage;
import com.bank.transaction_service.application.service.ConsumerHandleSuccessTransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsumerHandleSuccessServiceImpl implements ConsumerHandleSuccessTransactionService {

  @Override
  @RabbitListener(queues = "${james-config-rabbitmq.done-transaction.queue.queue-success}")
  public Mono<Void> handleSuccessTransaction(TransactionMessage transactionMessage) {
    return Mono.just(transactionMessage)
        .flatMap(
            message -> {
              log.info("Notify to user  {} is successful ", message);
              return Mono.empty();
            });
  }
}
