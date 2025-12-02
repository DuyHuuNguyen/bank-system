package com.bank.transaction_service.application.exception;

import com.bank.transaction_service.infrastructure.enums.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class IdempotencyException extends RuntimeException {
  private final String message;

  public IdempotencyException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.message = errorCode.getMessage();
  }
}
