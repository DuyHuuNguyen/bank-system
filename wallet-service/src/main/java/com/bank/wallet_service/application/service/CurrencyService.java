package com.bank.wallet_service.application.service;

import com.bank.wallet_service.domain.entity.Currency;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CurrencyService {
  Mono<Currency> save(Currency currency);

  Mono<Currency> findById(Long id);

  Flux<Currency> findAll(int page, int size);

  Mono<Currency> findByName(String name);
}
