package com.bank.auth_service.infrastructure.rest.controller;

import com.bank.auth_service.api.facade.RoleFacade;
import com.bank.auth_service.api.request.ChangeRoleAccountRequest;
import com.bank.auth_service.api.request.UpsertRoleRequest;
import com.bank.auth_service.api.response.BaseResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController {
  private final RoleFacade roleFacade;

  @PostMapping
  @ResponseStatus(HttpStatus.OK)
  @SecurityRequirement(name = "Bearer Authentication")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public Mono<BaseResponse<Void>> createRole(@RequestBody UpsertRoleRequest request) {
    return this.roleFacade.createRole(request);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  @SecurityRequirement(name = "Bearer Authentication")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public Mono<BaseResponse<Void>> removeRole(@PathVariable Long id) {
    return this.roleFacade.removeRole(id);
  }

  @PatchMapping("/role-account")
  @ResponseStatus(HttpStatus.OK)
  @SecurityRequirement(name = "Bearer Authentication")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public Mono<BaseResponse<Void>> changeRoleForAccount(
      @RequestBody ChangeRoleAccountRequest request) {
    return this.roleFacade.changeRoleForAccount(request);
  }
}
