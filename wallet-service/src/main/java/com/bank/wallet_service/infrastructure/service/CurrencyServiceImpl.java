package com.bank.wallet_service.infrastructure.service;

import com.bank.wallet_service.application.service.CurrencyService;
import com.bank.wallet_service.domain.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {
  private final CurrencyRepository currencyRepository;
}
