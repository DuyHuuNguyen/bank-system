package com.bank.wallet_service.domain.repository;

import com.bank.wallet_service.domain.entity.Wallet;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends R2dbcRepository<Wallet, Long> {}
