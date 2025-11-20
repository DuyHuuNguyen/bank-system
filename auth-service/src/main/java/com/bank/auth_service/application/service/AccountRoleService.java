package com.bank.auth_service.application.service;

import com.bank.auth_service.domain.entity.AccountRole;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountRoleService {
  Flux<AccountRole> findByAccountId(Long accountId);

  Mono<Void> deleteByAccountId(Long accountId);

  Flux<AccountRole> save(List<AccountRole> accountRoles);

  Mono<AccountRole> save(AccountRole accountRole);
}
