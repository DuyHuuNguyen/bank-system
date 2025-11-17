package com.bank.auth_service.api.request;

import com.bank.auth_service.infrastructure.nums.RoleEnum;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UpsertRoleRequest {
  @Hidden private Long id;
  private RoleEnum roleEnum;
}
