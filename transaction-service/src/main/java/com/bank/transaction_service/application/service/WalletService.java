package com.bank.transaction_service.application.service;

import java.math.BigDecimal;
import reactor.core.publisher.Mono;

public interface WalletService {
  Mono<Boolean> subBalanceWallet(Long id, BigDecimal amount, Long version);

  Mono<Boolean> addBalanceWallet(Long id, BigDecimal amount, Long version);

  Mono<Boolean> transfer(
      Long sourceWalletId,
      Long sourceVersion,
      Long destinationWalletId,
      Long destinationVersion,
      BigDecimal amount);
}
