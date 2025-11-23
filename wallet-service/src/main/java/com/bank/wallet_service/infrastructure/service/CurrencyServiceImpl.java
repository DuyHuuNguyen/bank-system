package com.bank.wallet_service.infrastructure.service;

import com.bank.wallet_service.application.service.CurrencyService;
import com.bank.wallet_service.domain.entity.Currency;
import com.bank.wallet_service.domain.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {
  private final CurrencyRepository currencyRepository;
  private final DatabaseClient databaseClient;

  @Override
  public Mono<Currency> save(Currency currency) {
    if (currency.getId() != null) currency.reUpdate();
    return this.currencyRepository.save(currency);
  }

  @Override
  public Mono<Currency> findById(Long id) {
    return this.currencyRepository.findById(id);
  }

  public Flux<Currency> findAll(int page, int size) {
    int offset = page * size;
    return databaseClient
        .sql("SELECT * FROM currencies LIMIT :size OFFSET :offset")
        .bind("size", size)
        .bind("offset", offset)
        .map(
            ((row, rowMetadata) -> {
              Currency currency =
                  Currency.builder()
                      .id(row.get("id", Long.class))
                      .currencyName(row.get("currency_name", String.class))
                      .isActive(row.get("is_active", Boolean.class))
                      .updatedAt(row.get("updated_at", Long.class))
                      .createdAt(row.get("created_at", Long.class))
                      .build();
              return currency;
            }))
        .all();
  }
}
