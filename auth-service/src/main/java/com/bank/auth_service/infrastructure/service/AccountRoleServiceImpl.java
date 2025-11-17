package com.bank.auth_service.infrastructure.service;

import com.bank.auth_service.application.service.AccountRoleService;
import com.bank.auth_service.domain.entity.AccountRole;
import com.bank.auth_service.domain.respository.AccountRoleRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AccountRoleServiceImpl implements AccountRoleService {
  private final AccountRoleRepository accountRoleRepository;

  @Override
  public Flux<AccountRole> findByAccountId(Long accountId) {
    return this.accountRoleRepository.findByAccountId(accountId);
  }

  @Override
  public Mono<Void> deleteByAccountId(Long accountId) {
    return this.accountRoleRepository.deleteByAccountId(accountId);
  }

  @Override
  public Flux<AccountRole> save(List<AccountRole> accountRoles) {
    return this.accountRoleRepository.saveAll(accountRoles);
  }
}
