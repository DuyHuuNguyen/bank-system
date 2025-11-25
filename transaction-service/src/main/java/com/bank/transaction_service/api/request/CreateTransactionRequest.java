package com.bank.transaction_service.api.request;

import com.bank.transaction_service.infrastructure.enums.PaymentRouting;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
public class CreateTransactionRequest {
  private String idempotencyKey;
  private PaymentRouting paymentRouting;
  private Long fromWalletId;
  private Long toWalletId;
  private String description;
  private BigDecimal amount;
  private Long methodId;
}
