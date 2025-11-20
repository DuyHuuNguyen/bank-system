package com.bank.auth_service.infrastructure.service;

import com.bank.auth_service.application.service.RoleService;
import com.bank.auth_service.domain.entity.Role;
import com.bank.auth_service.domain.respository.RoleRepository;
import com.bank.auth_service.infrastructure.nums.RoleEnum;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
  private final RoleRepository roleRepository;

  @Override
  public Flux<Role> findRolesByAccountId(Long accountId) {
    return this.roleRepository.findRolesByAccountId(accountId);
  }

  @Override
  public Mono<Role> save(Role role) {
    if (!role.isFirstSave()) role.reUpdate();
    return this.roleRepository.save(role);
  }

  @Override
  public Mono<Void> deleteById(Long id) {
    return this.roleRepository.deleteById(id);
  }

  @Override
  public Flux<Role> findByIds(List<Long> ids) {
    return this.roleRepository.findByIds(ids);
  }

  @Override
  public Mono<Role> findByRoleName(RoleEnum roleEnum) {
    return this.roleRepository.findByRoleName(roleEnum);
  }
}
