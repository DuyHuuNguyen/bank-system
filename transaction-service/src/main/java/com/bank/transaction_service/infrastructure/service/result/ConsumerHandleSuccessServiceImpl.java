package com.bank.transaction_service.infrastructure.service.result;

import com.bank.transaction_service.api.response.TransactionResponse;
import com.bank.transaction_service.application.message.TransactionMessage;
import com.bank.transaction_service.application.service.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsumerHandleSuccessServiceImpl implements ConsumerHandleSuccessTransactionService {

  private final CacheTransactionHistoryService cacheTransactionHistoryService;

  @Override
  @RabbitListener(queues = "${james-config-rabbitmq.done-transaction.queue.queue-success}")
  public Mono<Void> handleSuccessTransaction(TransactionMessage transactionMessage) {
    log.info("Successful transaction -> : {}", transactionMessage);
    return Mono.just(transactionMessage)
        .flatMap(
            message -> {
              log.info("Notify to user  {} is successful ", message);
              long walletId = transactionMessage.getSourceWalletId();

              LocalDate localDateCreatedAt =
                  Instant.ofEpochMilli(transactionMessage.getCreatedAt())
                      .atZone(ZoneId.systemDefault())
                      .toLocalDate();

              TransactionResponse transactionResponse =
                  TransactionResponse.builder()
                      .id(transactionMessage.getTransactionId())
                      .balance(transactionMessage.getAmount().toString())
                      .sourceWalletId(walletId)
                      .destinationWalletId(transactionMessage.getDestinationWalletId())
                      .description(transactionMessage.getDescription())
                      .createdAt(transactionMessage.getCreatedAt())
                      .methodName(transactionMessage.getTransactionMethodEnum().toString())
                      .status(transactionMessage.getStatus().toString())
                      .type(transactionMessage.getPaymentRouting().toString())
                      .build();

              return this.cacheTransactionHistoryService
                  .cacheTransaction(walletId, transactionResponse, localDateCreatedAt)
                  .doOnSuccess(ok -> log.info(" Ok cached"))
                  .then();
            });
  }
}
