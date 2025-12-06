package com.bank.transaction_service.infrastructure.service;

import com.bank.transaction_service.application.service.WalletGrpcClientService;
import com.example.server.wallet.*;
import java.util.Objects;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class WalletGrpcClientServiceImpl implements WalletGrpcClientService {
  @GrpcClient("wallet-service")
  private WalletServiceGrpc.WalletServiceBlockingStub walletServiceBlockingStub;

  @Override
  public Mono<WalletResponse> findWalletByRequest(WalletRequest request) {
    return Mono.fromCallable(() -> this.walletServiceBlockingStub.findWalletByRequest(request))
        .materialize()
        .flatMap(
            signal -> {
              if (signal.isOnError()) {
                return Mono.just(WalletResponse.newBuilder().build());
              }
              return Mono.just(Objects.requireNonNull(signal.get()));
            });
  }

  @Override
  public Mono<WalletResponse> findWalletOwnerByRequest(WalletOwnerRequest request) {
    return Mono.fromCallable(() -> this.walletServiceBlockingStub.findWalletOwnerByRequest(request))
        .materialize()
        .flatMap(
            signal -> {
              if (signal.isOnError()) {
                return Mono.just(WalletResponse.newBuilder().build());
              }
              return Mono.just(Objects.requireNonNull(signal.get()));
            });
  }

  @Override
  public Mono<WalletProfileResponse> findWalletProfileByRequest(WalletProfileRequest request) {
    return Mono.fromCallable(
            () -> this.walletServiceBlockingStub.findWalletProfileByRequest(request))
        .materialize()
        .flatMap(
            signal -> {
              if (signal.isOnError()) {
                return Mono.just(WalletProfileResponse.newBuilder().build());
              }
              return Mono.just(Objects.requireNonNull(signal.get()));
            });
  }
}
