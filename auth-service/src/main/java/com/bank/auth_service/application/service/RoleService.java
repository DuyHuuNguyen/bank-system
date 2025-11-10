package com.bank.auth_service.application.service;

import com.bank.auth_service.domain.entity.Role;
import reactor.core.publisher.Flux;

public interface RoleService {
  Flux<Role> findRolesByAccountId(Long accountId);
}
