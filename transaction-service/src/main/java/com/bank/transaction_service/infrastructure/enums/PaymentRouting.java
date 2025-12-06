package com.bank.transaction_service.infrastructure.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentRouting {
  TRANSFER("wallet.transfer.#"),
  DEPOSIT("wallet.deposit.#"),
  WITHDRAW("wallet.withdraw.#");

  private final String routing;
}
