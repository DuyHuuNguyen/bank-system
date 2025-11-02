package com.bank.auth_service.application.exception;

import com.bank.auth_service.infrastructure.nums.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CacheException extends RuntimeException {
  private final String message;

  public CacheException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.message = errorCode.getMessage();
  }
}
