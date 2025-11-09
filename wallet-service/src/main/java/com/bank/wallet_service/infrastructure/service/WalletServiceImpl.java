package com.bank.wallet_service.infrastructure.service;

import com.bank.wallet_service.application.service.WalletService;
import com.bank.wallet_service.domain.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {
  private final WalletRepository walletRepository;
}
