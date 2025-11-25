package com.bank.transaction_service.infrastructure.service;

import com.bank.transaction_service.application.service.WalletService;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class WalletServiceImpl implements WalletService {

  @Override
  public Mono<Long> subBalanceWallet(Long id, BigDecimal amount, Long version) {
    return null;
  }

  @Override
  public Mono<Long> addBalanceWallet(Long id, BigDecimal amount, Long version) {
    return null;
  }
}
