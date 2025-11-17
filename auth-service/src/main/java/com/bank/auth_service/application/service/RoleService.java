package com.bank.auth_service.application.service;

import com.bank.auth_service.domain.entity.Role;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RoleService {
  Flux<Role> findRolesByAccountId(Long accountId);

  Mono<Role> save(Role role);

  Mono<Void> deleteById(Long id);

  Flux<Role> findByIds(List<Long> ids);
}
