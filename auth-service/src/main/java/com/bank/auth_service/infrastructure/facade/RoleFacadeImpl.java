package com.bank.auth_service.infrastructure.facade;

import com.bank.auth_service.api.facade.RoleFacade;
import com.bank.auth_service.api.request.ChangeRoleAccountRequest;
import com.bank.auth_service.api.request.UpsertRoleRequest;
import com.bank.auth_service.api.response.BaseResponse;
import com.bank.auth_service.application.exception.EntityNotFoundException;
import com.bank.auth_service.application.service.AccountRoleService;
import com.bank.auth_service.application.service.RoleService;
import com.bank.auth_service.domain.entity.AccountRole;
import com.bank.auth_service.domain.entity.Role;
import com.bank.auth_service.infrastructure.nums.ErrorCode;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RoleFacadeImpl implements RoleFacade {
  private final RoleService roleService;
  private final AccountRoleService accountRoleService;

  @Override
  @Transactional
  public Mono<BaseResponse<Void>> createRole(UpsertRoleRequest request) {
    Role role = Role.builder().roleName(request.getRoleEnum()).build();
    return this.roleService
        .save(role)
        .switchIfEmpty(Mono.error(new EntityNotFoundException(ErrorCode.ROLE_NOT_FOUND)))
        .thenReturn(BaseResponse.ok());
  }

  @Override
  @Transactional
  public Mono<BaseResponse<Void>> removeRole(Long id) {
    return this.roleService.deleteById(id).thenReturn(BaseResponse.ok());
  }

  @Override
  @Transactional
  public Mono<BaseResponse<Void>> changeRoleForAccount(ChangeRoleAccountRequest request) {
    return Mono.zip(
            this.accountRoleService.deleteByAccountId(request.getAccountId()),
            this.createAccountRole(request))
        .map(stuple2 -> BaseResponse.ok());
  }

  private Mono<Void> createAccountRole(ChangeRoleAccountRequest request) {
    return this.roleService
        .findByIds(request.getRoleIds())
        .collectList()
        .flatMap(
            roles -> {
              boolean isValidRole = roles.size() == request.getRoleIds().size();
              if (!isValidRole)
                return Mono.error(new EntityNotFoundException(ErrorCode.ROLE_NOT_FOUND));
              List<AccountRole> accountRoles = new ArrayList<>();
              for (Role role : roles) {
                AccountRole newAccountRole =
                    AccountRole.builder()
                        .accountId(request.getAccountId())
                        .roleId(role.getId())
                        .build();
                accountRoles.add(newAccountRole);
              }
              return this.accountRoleService
                  .save(accountRoles)
                  .switchIfEmpty(Mono.error(new EntityNotFoundException(ErrorCode.ROLE_NOT_FOUND)))
                  .then();
            });
  }
}
