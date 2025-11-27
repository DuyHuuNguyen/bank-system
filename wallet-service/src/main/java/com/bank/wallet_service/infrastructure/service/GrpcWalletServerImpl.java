package com.bank.wallet_service.infrastructure.service;

import com.bank.wallet_service.application.dto.WalletCurrencyDTO;
import com.bank.wallet_service.application.exception.EntityNotFoundException;
import com.bank.wallet_service.application.service.WalletService;
import com.bank.wallet_service.infrastructure.enums.ErrorCode;
import com.example.server.wallet.*;
import io.grpc.stub.StreamObserver;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import reactor.core.publisher.Mono;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class GrpcWalletServerImpl extends WalletServiceGrpc.WalletServiceImplBase {
  private final WalletService walletService;

  @Override
  public void findWalletOwnerByRequest(
      WalletOwnerRequest request, StreamObserver<WalletResponse> responseObserver) {
    this.walletService
        .findWalletCurrencyByUserIdAndWalletId(request.getUserId(), request.getWalletId())
        .switchIfEmpty(Mono.error(new EntityNotFoundException(ErrorCode.WALLET_NOT_FOUND)))
        .flatMap(this::buildWalletResponse)
        .doOnNext(ok -> log.info("resp {} ", ok))
        .onErrorResume(
            err -> {
              log.error("Error while finding wallet", err);
              return Mono.just(
                  WalletResponse.newBuilder()
                      .setId(-1)
                      .setUserId(-1)
                      .setAvailableBalance("")
                      .setCurrency("")
                      .setFullName("Not found")
                      .setVersion(-1)
                      .build());
            })
        .subscribe(
            response -> {
              responseObserver.onNext(response);
              responseObserver.onCompleted();
            });
  }

  @Override
  public void findWalletByRequest(
      WalletRequest request, StreamObserver<WalletResponse> responseObserver) {
    this.walletService
        .findWalletById(request.getWalletId())
        .switchIfEmpty(Mono.error(new EntityNotFoundException(ErrorCode.WALLET_NOT_FOUND)))
        .flatMap(this::buildWalletResponse)
        .doOnError(err -> log.error("PIPELINE ERROR !!!", err))
        .doOnNext(ok -> log.info("resp {} ", ok))
        .onErrorResume(
            err -> {
              log.error("Error while finding wallet", err);
              return Mono.just(
                  WalletResponse.newBuilder()
                      .setId(-1)
                      .setUserId(-1)
                      .setAvailableBalance("")
                      .setCurrency("")
                      .setFullName("Not found")
                      .setVersion(-1)
                      .build());
            })
        .subscribe(
            response -> {
              responseObserver.onNext(response);
              responseObserver.onCompleted();
            });
  }

  private Mono<WalletResponse> buildWalletResponse(WalletCurrencyDTO walletCurrencyDTO) {
    return Mono.just(
        WalletResponse.newBuilder()
            .setId(walletCurrencyDTO.getId())
            .setUserId(Optional.ofNullable(walletCurrencyDTO.getUserId()).orElse(-1L))
            .setAvailableBalance(
                Optional.ofNullable(walletCurrencyDTO.getAvailableBalance().toString()).orElse(""))
            .setCurrency(Optional.ofNullable(walletCurrencyDTO.getCurrencyName()).orElse(""))
            .setFullName("James dev java")
            .setVersion(Optional.ofNullable(walletCurrencyDTO.getVersion()).orElse(-1L))
            .build());
  }
}
