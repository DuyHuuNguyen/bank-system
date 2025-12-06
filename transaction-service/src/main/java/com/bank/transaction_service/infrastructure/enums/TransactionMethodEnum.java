package com.bank.transaction_service.infrastructure.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TransactionMethodEnum {
  ATM("ATM"),
  MOBILE("MOBILE"),
  DEPARTMENT("DEPARTMENT");
  private final String method;
}
