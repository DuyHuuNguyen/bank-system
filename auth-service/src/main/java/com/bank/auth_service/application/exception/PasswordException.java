package com.bank.auth_service.application.exception;

import com.bank.auth_service.infrastructure.nums.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PasswordException extends RuntimeException {
  private final String message;

  public PasswordException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.message = errorCode.getMessage();
  }
}
