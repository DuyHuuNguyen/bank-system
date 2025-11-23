package com.bank.wallet_service.domain.repository;

import com.bank.wallet_service.domain.entity.Currency;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CurrencyRepository extends R2dbcRepository<Currency, Long> {
  Mono<Currency> findByCurrencyName(String name);
}
