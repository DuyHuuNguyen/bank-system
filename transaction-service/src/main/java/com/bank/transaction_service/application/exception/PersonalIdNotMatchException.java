package com.bank.transaction_service.application.exception;

import com.bank.transaction_service.infrastructure.enums.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PersonalIdNotMatchException extends RuntimeException {
  private String message;

  public PersonalIdNotMatchException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.message = errorCode.getMessage();
  }
}
