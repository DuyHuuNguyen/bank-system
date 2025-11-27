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
  private final TransactionService transactionService;
  private final TransactionMethodService transactionMethodService;
  private final MethodService methodService;

  //    @RabbitListener(queues = "${james-config-rabbitmq.start-transaction.queue.transfer-queue}")
  public void hehe(TransactionMessage transactionMessage) {
    log.info(transactionMessage.toString());
    //        return Mono.empty();
  }
  //
  //    @PostConstruct
  //    void run(){
  //        WalletOwnerRequest fromWalletOwnerRequest =
  // WalletOwnerRequest.newBuilder().setWalletId(1L)
  //                .setUserId(3L).build();
  //        WalletRequest toWalletRequest = WalletRequest.newBuilder().setWalletId(1L).build();
  //        System.out.println("KKKKKKKKKKKKKKKKKKKKKKKKKKKk");
  //            this.walletGrpcClientService.findWalletOwnerByRequest(fromWalletOwnerRequest)
  //                .switchIfEmpty(Mono.error(new RuntimeException("miss exception for.. :)))")))
  //                    .map(ok ->{
  //                        log.info("ok {}",ok);
  //                        return ok;
  //                    })
  //                .subscribe();
  ////        Mono<WalletResponse> toWalletResponseMono =
  // this.walletGrpcClientService.findWalletByRequest(toWalletRequest)
  ////                .switchIfEmpty(Mono.error(new RuntimeException("miss exception for.. :)))")));
  //    }

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
        this.walletGrpcClientService
            .findWalletOwnerByRequest(fromWalletOwnerRequest)
            .switchIfEmpty(Mono.error(new RuntimeException("miss exception for.. :)))")));
    Mono<WalletResponse> toWalletResponseMono =
        this.walletGrpcClientService
            .findWalletByRequest(toWalletRequest)
            .switchIfEmpty(Mono.error(new RuntimeException("miss exception for.. :)))")));
    log.info("hello ... ");
    return Mono.zip(fromWalletReponseMono, toWalletResponseMono)
        .flatMap(
            sourceAndDestinationWallet -> {
              log.info("hello ...1");
              WalletResponse sourceWallet = sourceAndDestinationWallet.getT1();
              WalletResponse destinationWallet = sourceAndDestinationWallet.getT2();

              boolean isNotFoundSourceWallet = sourceWallet.getId() == -1;
              boolean isNotFoundDestinationWallet = sourceWallet.getId() == -1;

              if (isNotFoundSourceWallet || isNotFoundDestinationWallet) {
                log.info("Wallet not found and Message transaction will send to fail queue");
                return Mono.empty().then();
              }

              log.info("hello ...{}", sourceWallet.getVersion());
              log.info("hello ...{}", destinationWallet.getVersion());
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
              log.info("ok ?????  {} ", transaction.toString());

              return this.transactionService
                  .save(transaction)
                  .switchIfEmpty(Mono.error(new Exception("code di em")))
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
                                      .doOnSuccess(ok -> log.info("send request sub money"))
                                      .then();
                                });
                      });
            });
  }

  private Mono<Boolean> updateBalance(
      WalletResponse sourceWallet, WalletResponse destinationWallet, BigDecimal amount) {
    UpdateBalanceRequest addBalanceRequest =
        UpdateBalanceRequest.newBuilder()
            .setId(destinationWallet.getId())
            .setAmount(String.valueOf(amount))
            .setVersion(destinationWallet.getVersion())
            .build();
    UpdateBalanceRequest subBalanceRequest =
        UpdateBalanceRequest.newBuilder()
            .setId(sourceWallet.getId())
            .setAmount(String.valueOf(amount))
            .setVersion(sourceWallet.getVersion())
            .build();

    Mono<UpdateBalanceResponse> addBalanceResponseMono =
        this.walletGrpcClientService.addBalanceWallet(addBalanceRequest);
    Mono<UpdateBalanceResponse> subBalanceResponseMono =
        this.walletGrpcClientService.subBalanceWallet(subBalanceRequest);

    return Mono.zip(addBalanceResponseMono, subBalanceResponseMono)
        .flatMap(
            addBalanceAndSubBalanceResponse -> {
              UpdateBalanceResponse addBalanceResponse = addBalanceAndSubBalanceResponse.getT1();
              UpdateBalanceResponse subBalanceResponse = addBalanceAndSubBalanceResponse.getT2();
              //                  boolean isValidUpdate
              return null;
            });
  }
}
