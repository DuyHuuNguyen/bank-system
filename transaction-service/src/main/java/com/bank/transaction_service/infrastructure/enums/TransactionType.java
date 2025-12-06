package com.bank.transaction_service.infrastructure.enums;

import java.util.Arrays;

public enum TransactionType {
  DEPOSIT,
  WITHDRAW,
  TRANSFER;

  public static TransactionType findByName(PaymentRouting paymentRouting) {
    return Arrays.stream(values())
        .filter(transactionType -> transactionType.toString().equals(paymentRouting.toString()))
        .findFirst()
        .orElse(TransactionType.TRANSFER);
  }
}
