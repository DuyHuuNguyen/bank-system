package com.bank.wallet_service.application.service;

import com.bank.wallet_service.domain.entity.Wallet;
import reactor.core.publisher.Mono;

public interface WalletService {
  Mono<Wallet> save(Wallet wallet);
}
