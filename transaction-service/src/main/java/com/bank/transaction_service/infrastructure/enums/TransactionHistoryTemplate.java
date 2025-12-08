package com.bank.transaction_service.infrastructure.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TransactionHistoryTemplate {
  TRANSACTION_HISTORY_TEMPLATE_KEY("transaction_history_template_wallet_id_%s_date_%s");
  private final String content;
}
