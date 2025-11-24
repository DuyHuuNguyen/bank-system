package com.bank.wallet_service.infrastructure.service;

import com.bank.wallet_service.application.dto.WalletCurrencyDTO;
import com.bank.wallet_service.application.exception.EntityNotFoundException;
import com.bank.wallet_service.application.service.UserService;
import com.bank.wallet_service.application.service.WalletService;
import com.bank.wallet_service.infrastructure.enums.ErrorCode;
import com.example.server.wallet.WalletOwnerRequest;
import com.example.server.wallet.WalletRequest;
import com.example.server.wallet.WalletResponse;
import com.example.server.wallet.WalletServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import reactor.core.publisher.Mono;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class GrpcWalletServerImpl extends WalletServiceGrpc.WalletServiceImplBase {
  private final WalletService walletService;
  private final UserService userService;

  @Override
  public void findWalletOwnerByRequest(
      WalletOwnerRequest request, StreamObserver<WalletResponse> responseObserver) {
    this.walletService
        .findWalletCurrencyByUserIdAndWalletId(request.getWalletId(), request.getUserId())
        .switchIfEmpty(Mono.error(new EntityNotFoundException(ErrorCode.WALLET_NOT_FOUND)))
        .flatMap(this::buildWalletResponse)
        .doOnError(err -> log.error("PIPELINE ERROR !!!", err))
        .doOnNext(ok -> log.info("resp {} ", ok))
        .subscribe(
            response -> {
              log.info("{}", response.toString());
              responseObserver.onNext(response);
              responseObserver.onCompleted();
            },
            responseObserver::onError);
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
        .subscribe(
            response -> {
              log.info("{}", response.toString());
              responseObserver.onNext(response);
              responseObserver.onCompleted();
            },
            responseObserver::onError);
  }

  private Mono<WalletResponse> buildWalletResponse(WalletCurrencyDTO walletCurrencyDTO) {
    return Mono.just(
        WalletResponse.newBuilder()
            .setUserId(walletCurrencyDTO.getUserId())
            .setAvailableBalance(walletCurrencyDTO.getAvailableBalance().toString())
            .setCurrency(walletCurrencyDTO.getCurrency())
            .setFullName("James dev java")
            .build());
  }
}
