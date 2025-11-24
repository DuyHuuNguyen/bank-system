package com.bank.transaction_service.infrastructure.service;

import com.bank.transaction_service.application.service.WalletGrpcClientService;
import com.example.server.wallet.WalletOwnerRequest;
import com.example.server.wallet.WalletRequest;
import com.example.server.wallet.WalletResponse;
import com.example.server.wallet.WalletServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class WalletGrpcClientServiceImpl implements WalletGrpcClientService {
  @GrpcClient("wallet-service")
  private WalletServiceGrpc.WalletServiceBlockingStub walletServiceBlockingStub;

  @Override
  public Mono<WalletResponse> findWalletByRequest(WalletRequest request) {
    return Mono.fromCallable(() -> this.walletServiceBlockingStub.findWalletByRequest(request));
    // add handle exception
  }

  @Override
  public Mono<WalletResponse> findWalletOwnerByRequest(WalletOwnerRequest request) {
    return Mono.fromCallable(
        () -> this.walletServiceBlockingStub.findWalletOwnerByRequest(request));
    //  add handle exception
  }
}
