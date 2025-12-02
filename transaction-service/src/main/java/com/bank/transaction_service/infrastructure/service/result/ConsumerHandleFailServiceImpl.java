package com.bank.transaction_service.infrastructure.service.result;

import com.bank.transaction_service.application.message.TransactionMessage;
import com.bank.transaction_service.application.service.ConsumerHandleFailTransactionService;
import com.bank.transaction_service.application.service.MethodService;
import com.bank.transaction_service.application.service.TransactionMethodService;
import com.bank.transaction_service.application.service.TransactionService;
import com.bank.transaction_service.domain.entity.Transaction;
import com.bank.transaction_service.infrastructure.enums.TransactionStatus;
import com.bank.transaction_service.infrastructure.enums.TransactionType;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsumerHandleFailServiceImpl implements ConsumerHandleFailTransactionService {

  private final TransactionService transactionService;
  private final MethodService methodService;
  private final TransactionMethodService transactionMethodService;

  @Override
  @RabbitListener(queues = "${james-config-rabbitmq.fail-transaction.queue.queue-fail}")
  public Mono<Void> handleFailTransaction(TransactionMessage transactionMessage) {
    return Mono.just(transactionMessage)
        .flatMap(
            message -> {
              log.info("Notify to user  {} is fail", message);
              Transaction transaction =
                  Transaction.builder()
                      .sourceWalletId(transactionMessage.getSourceWalletId())
                      .beneficiaryWalletId(transactionMessage.getDestinationWalletId())
                      .currency("")
                      .transactionBalance(transactionMessage.getAmount())
                      .referenceCode(UUID.randomUUID().toString())
                      .type(TransactionType.findByName(transactionMessage.getPaymentRouting()))
                      .createdAt(transactionMessage.getCreatedAt())
                      .description(transactionMessage.getDescription())
                      .status(TransactionStatus.FAIL)
                      .build();
              return this.transactionService
                  .save(transaction)
                  .doOnError(error -> log.error("Can't save transaction {}", transaction))
                  .flatMap(
                      transactionStored -> {
                        return this.methodService
                            .findByMethodName(
                                transactionMessage.getTransactionMethodEnum().getMethod())
                            .doOnError(error -> log.warn("Can't found transaction-method by name"))
                            .flatMap(
                                method -> {
                                  com.bank.transaction_service.domain.entity.TransactionMethod
                                      transactionMethod =
                                          com.bank.transaction_service.domain.entity
                                              .TransactionMethod.builder()
                                              .methodId(method.getId())
                                              .transactionId(transactionStored.getId())
                                              .build();
                                  return this.transactionMethodService
                                      .save(transactionMethod)
                                      .doOnError(
                                          error ->
                                              log.error(
                                                  "Can't save transaction-method {}",
                                                  transactionMethod))
                                      .then();
                                });
                      });
            });
  }
}
