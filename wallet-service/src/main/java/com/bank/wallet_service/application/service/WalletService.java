package com.bank.wallet_service.application.service;

import com.bank.wallet_service.application.dto.WalletCurrencyDTO;
import com.bank.wallet_service.domain.entity.Wallet;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface WalletService {
  Mono<Wallet> save(Wallet wallet);

  Flux<WalletCurrencyDTO> findWalletCurrencyByUserId(Long userId);
  Mono<WalletCurrencyDTO> findWalletCurrencyByUserIdAndWalletId(Long userId,Long walletId);
  Mono<WalletCurrencyDTO> findWalletById(Long id);
}
