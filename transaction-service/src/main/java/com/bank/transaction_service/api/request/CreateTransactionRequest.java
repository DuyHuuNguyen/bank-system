package com.bank.transaction_service.api.request;

import com.bank.transaction_service.infrastructure.enums.PaymentRouting;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CreateTransactionRequest {
  private String IdempotencyKey;
  private PaymentRouting paymentRouting;
  private Long fromWalletId;
  private Long toWalletId;
  private String description;
  private BigDecimal amount;
}
