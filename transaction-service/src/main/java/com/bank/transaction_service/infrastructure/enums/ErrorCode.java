package com.bank.transaction_service.infrastructure.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
  WALLET_NOT_FOUND("Wallet not found");
  private final String message;
}
