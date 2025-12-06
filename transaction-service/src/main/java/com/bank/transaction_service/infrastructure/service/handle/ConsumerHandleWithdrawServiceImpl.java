package com.bank.transaction_service.infrastructure.service.handle;

import com.bank.transaction_service.application.message.TransactionMessage;
import com.bank.transaction_service.application.service.*;
import com.bank.transaction_service.domain.entity.Transaction;
import com.bank.transaction_service.infrastructure.enums.TransactionMessageStatus;
import com.bank.transaction_service.infrastructure.enums.TransactionStatus;
import com.bank.transaction_service.infrastructure.enums.TransactionType;
import com.example.server.wallet.WalletOwnerRequest;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsumerHandleWithdrawServiceImpl implements ConsumerHandleWithdrawService {
  private final WalletGrpcClientService walletGrpcClientService;
  private final ProducerFailTransactionService producerFailTransactionService;
  private final ProducerSuccessTransactionService producerSuccessTransactionService;
  private final TransactionService transactionService;
  private final TransactionMethodService transactionMethodService;
  private final MethodService methodService;
  private final WalletService walletService;

  @Override
  @RabbitListener(queues = "${james-config-rabbitmq.start-transaction.queue.withdraw-queue}")
  public Mono<Void> consumeDepositMessage(TransactionMessage transactionMessage) {
    log.info(transactionMessage.toString());
    boolean isWithDrawMessage =
        Objects.equals(
            transactionMessage.getDestinationWalletId(), transactionMessage.getSourceWalletId());
    if (!isWithDrawMessage) {
      transactionMessage.changeStatus(TransactionMessageStatus.ERROR_DEPOSIT);
      return producerFailTransactionService.sendFailTransactionMessage(transactionMessage);
    }
    WalletOwnerRequest walletOwnerRequest =
        WalletOwnerRequest.newBuilder()
            .setWalletId(transactionMessage.getSourceWalletId())
            .setUserId(transactionMessage.getOwnerTransactionId())
            .build();
    return this.walletGrpcClientService
        .findWalletOwnerByRequest(walletOwnerRequest)
        .flatMap(
            walletResponse -> {
              boolean isNotFoundWallet = walletResponse.getId() == -1;
              if (isNotFoundWallet) {
                log.info("Not found wallet for deposit");
                return producerFailTransactionService.sendFailTransactionMessage(
                    transactionMessage);
              }
              Transaction transaction =
                  Transaction.builder()
                      .sourceWalletId(walletResponse.getId())
                      .beneficiaryWalletId(walletResponse.getId())
                      .currency(walletResponse.getCurrency())
                      .transactionBalance(transactionMessage.getAmount())
                      .referenceCode(UUID.randomUUID().toString())
                      .type(TransactionType.WITHDRAW)
                      .createdAt(transactionMessage.getCreatedAt())
                      .description(transactionMessage.getDescription())
                      .status(TransactionStatus.SUCCESSFUL)
                      .build();
              log.info("{}", transaction.toString());

              return this.walletService
                  .subBalanceWallet(
                      walletResponse.getId(),
                      transactionMessage.getAmount(),
                      walletResponse.getVersion())
                  .flatMap(
                      isDoneDeposit -> {
                        if (!isDoneDeposit) {
                          log.info("can not deposit");
                          return this.producerFailTransactionService.sendFailTransactionMessage(
                              transactionMessage);
                        }
                        return this.transactionService
                            .save(transaction)
                            .doOnError(error -> log.error("Can't save transaction {}", transaction))
                            .flatMap(
                                transactionStored -> {
                                  return this.methodService
                                      .findByMethodName(
                                          transactionMessage.getTransactionMethodEnum().getMethod())
                                      .flatMap(
                                          method -> {
                                            com.bank.transaction_service.domain.entity
                                                    .TransactionMethod
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
                                                .flatMap(
                                                    transactionMethodStored ->
                                                        this.producerSuccessTransactionService
                                                            .sendSuccessTransactionMessage(
                                                                transactionMessage))
                                                .doOnSuccess(
                                                    ok -> log.info("Done withdraw process"))
                                                .then();
                                          });
                                });
                      });
            });
  }
}
