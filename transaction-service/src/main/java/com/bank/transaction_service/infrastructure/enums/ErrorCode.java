package com.bank.transaction_service.infrastructure.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
  WALLET_NOT_FOUND("Wallet not found"),
  DUPLICATED_TRANSACTION("Duplicated transaction"),
  TRANSACTION_NOT_FOUND("Transaction not found"),
  OTP_NOT_MATCH("Otp don't match"),
  PERSONAL_ID_NOT_MATCH("Personal Id not match"),
  CAN_NOT_UPDATE_OTP("Can't update opt");
  private final String message;
}
