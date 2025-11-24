package com.bank.wallet_service.infrastructure.service;

import com.bank.wallet_service.application.dto.WalletCurrencyDTO;
import com.bank.wallet_service.application.service.WalletService;
import com.bank.wallet_service.domain.entity.Wallet;
import com.bank.wallet_service.domain.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {
  private final WalletRepository walletRepository;

  @Override
  public Mono<Wallet> save(Wallet wallet) {
    if (wallet.getId() != null) wallet.reUpdate();
    return this.walletRepository.save(wallet);
  }

  @Override
  public Flux<WalletCurrencyDTO> findWalletCurrencyByUserId(Long userId) {
    return this.walletRepository.findAllByUserId(userId);
  }

  @Override
  public Mono<WalletCurrencyDTO> findWalletCurrencyByUserIdAndWalletId(Long userId, Long walletId) {
    return walletRepository.findByUserIdAndWalletId(userId, walletId);
  }

  @Override
  public Mono<WalletCurrencyDTO> findWalletById(Long id) {
    return this.walletRepository.findWalletById(id);
  }
}
