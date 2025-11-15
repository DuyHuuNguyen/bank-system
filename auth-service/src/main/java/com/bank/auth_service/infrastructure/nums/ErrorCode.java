package com.bank.auth_service.infrastructure.nums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
  JWT_INVALID("Jwt invalid"),
  ACCOUNT_NOT_FOUND("Account not found"),
  ROLE_NOT_FOUND("Role not found"),
  STORE_IS_ERROR("System error"),
  REFRESH_TOKEN_NOT_FOUND("Refresh token not found");

  private final String message;
}
