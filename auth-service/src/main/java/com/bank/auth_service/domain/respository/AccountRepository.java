package com.bank.auth_service.domain.respository;

import com.bank.auth_service.domain.entity.Account;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface AccountRepository extends R2dbcRepository<Account, Long> {

  Mono<Account> findByPersonalId(String personalId);

  Mono<Account> findByUserId(Long userId);
}
