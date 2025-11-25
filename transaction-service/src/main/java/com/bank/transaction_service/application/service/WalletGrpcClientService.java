package com.bank.transaction_service.application.service;

import com.example.server.wallet.*;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface WalletGrpcClientService {

  Mono<WalletResponse> findWalletByRequest(WalletRequest request);

  Mono<WalletResponse> findWalletOwnerByRequest(WalletOwnerRequest walletOwnerRequest);

}