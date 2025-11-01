package com.bank.auth_service.application.exception;

import com.bank.auth_service.infrastructure.nums.ErrorCode;
import lombok.Getter;

@Getter
public class UnauthorizedException extends RuntimeException {
  private final String message;

  public UnauthorizedException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.message = errorCode.getMessage();
  }
}
