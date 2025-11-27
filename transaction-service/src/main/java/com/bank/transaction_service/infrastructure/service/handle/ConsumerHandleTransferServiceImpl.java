package com.bank.transaction_service.infrastructure.service.handle;

import com.bank.transaction_service.application.message.TransactionMessage;
import com.bank.transaction_service.application.service.*;
import com.bank.transaction_service.domain.entity.Transaction;
import com.bank.transaction_service.domain.entity.TransactionMethod;
import com.bank.transaction_service.infrastructure.enums.TransactionMessageStatus;
import com.bank.transaction_service.infrastructure.enums.TransactionStatus;
import com.bank.transaction_service.infrastructure.enums.TransactionType;
import com.example.server.wallet.*;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsumerHandleTransferServiceImpl implements ConsumerHandleTransferService {
  private final WalletGrpcClientService walletGrpcClientService;
  private final ProducerFailTransactionService producerFailTransactionService;
  private final ProducerSuccessTransactionService producerSuccessTransactionService;
  private final TransactionService transactionService;
  private final TransactionMethodService transactionMethodService;
  private final MethodService methodService;
  private final WalletService walletService;

  @Override
  @Transactional
  @RabbitListener(queues = "${james-config-rabbitmq.start-transaction.queue.transfer-queue}")
  public Mono<Void> consumeTransferMessage(TransactionMessage transactionMessage) {
    log.info(transactionMessage.toString());

    WalletOwnerRequest fromWalletOwnerRequest =
        WalletOwnerRequest.newBuilder()
            .setWalletId(transactionMessage.getSourceWalletId())
            .setUserId(transactionMessage.getOwnerTransactionId())
            .build();
    WalletRequest toWalletRequest =
        WalletRequest.newBuilder().setWalletId(transactionMessage.getDestinationWalletId()).build();

    Mono<WalletResponse> fromWalletReponseMono =
        this.walletGrpcClientService.findWalletOwnerByRequest(fromWalletOwnerRequest);
    Mono<WalletResponse> toWalletResponseMono =
        this.walletGrpcClientService.findWalletByRequest(toWalletRequest);

    return Mono.zip(fromWalletReponseMono, toWalletResponseMono)
        .flatMap(
            sourceAndDestinationWallet -> {
              WalletResponse sourceWallet = sourceAndDestinationWallet.getT1();
              WalletResponse destinationWallet = sourceAndDestinationWallet.getT2();

              boolean isNotFoundSourceWallet = sourceWallet.getId() == -1;
              boolean isNotFoundDestinationWallet = sourceWallet.getId() == -1;

              if (isNotFoundSourceWallet || isNotFoundDestinationWallet) {
                log.info("Wallet not found and Message transaction will send to fail queue");
                return this.producerFailTransactionService
                    .sendFailTransactionMessage(transactionMessage)
                    .doOnSuccess(ok -> log.info("Fail transfer process"));
              }
              BigDecimal availableBalance = new BigDecimal(sourceWallet.getAvailableBalance());
              boolean isInsufficientFunds =
                  availableBalance.compareTo(transactionMessage.getAmount()) < 0;

              boolean isSameCurrency =
                  sourceWallet.getCurrency().equals(destinationWallet.getCurrency());

              if (isInsufficientFunds) {
                log.info("insufficient funds and message transaction will send to fail queue");

                transactionMessage.changeStatus(TransactionMessageStatus.INSUFFICIENT_FUNDS);
                return this.producerFailTransactionService.sendFailTransactionMessage(
                    transactionMessage);
              }

              if (!isSameCurrency) {
                log.info("Not fund api transfer not same currency");
                return Mono.empty().then();
              }

              Transaction transaction =
                  Transaction.builder()
                      .sourceWalletId(sourceWallet.getId())
                      .beneficiaryWalletId(destinationWallet.getId())
                      .currency(sourceWallet.getCurrency())
                      .transactionBalance(transactionMessage.getAmount())
                      .referenceCode(UUID.randomUUID().toString())
                      .type(TransactionType.TRANSFER)
                      .createdAt(transactionMessage.getCreatedAt())
                      .description(transactionMessage.getDescription())
                      .status(TransactionStatus.SUCCESSFUL)
                      .build();
              log.info("{}", transaction.toString());

              return this.walletService
                  .transfer(
                      sourceWallet.getId(),
                      sourceWallet.getVersion(),
                      destinationWallet.getId(),
                      destinationWallet.getVersion(),
                      transactionMessage.getAmount())
                  .flatMap(
                      isDoneTransfer -> {
                        log.info("!!!! importance ???? Is done transfer {}", isDoneTransfer);
                        if (!isDoneTransfer) {
                          transactionMessage.changeStatus(
                              TransactionMessageStatus.INSUFFICIENT_FUNDS);
                          return this.producerFailTransactionService
                              .sendFailTransactionMessage(transactionMessage)
                              .doOnSuccess(ok -> log.info("Fail transfer process"))
                              .then();
                        }
                        log.info("-> go to store transaction and method");
                        return this.transactionService
                            .save(transaction)
                            .flatMap(
                                transactionStored -> {
                                  return this.methodService
                                      .findById(transactionMessage.getMethodId())
                                      .flatMap(
                                          method -> {
                                            TransactionMethod transactionMethod =
                                                TransactionMethod.builder()
                                                    .methodId(method.getId())
                                                    .transactionId(transactionStored.getId())
                                                    .build();

                                            return this.transactionMethodService
                                                .save(transactionMethod)
                                                .flatMap(
                                                    transactionMethodStored ->
                                                        this.producerSuccessTransactionService
                                                            .sendSuccessTransactionMessage(
                                                                transactionMessage))
                                                .doOnSuccess(
                                                    ok -> log.info("Done transfer process"))
                                                .then();
                                          });
                                });
                      });
            });
  }
}
