package com.bank.wallet_service.domain.repository;

import com.bank.wallet_service.domain.entity.Currency;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends R2dbcRepository<Currency, Long> {
}
