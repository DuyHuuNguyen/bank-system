package com.bank.auth_service.infrastructure.nums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleEnum {
  USER("ROLE_USER"),
  ADMIN("ROLE_ADMIN");
  private final String name;
}
