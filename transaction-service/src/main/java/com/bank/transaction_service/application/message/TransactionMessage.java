package com.bank.transaction_service.application.message;

import com.bank.transaction_service.infrastructure.enums.PaymentRouting;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class TransactionMessage {
  private PaymentRouting paymentRouting;
  private Long ownerTransactionId;
  private Long sourceWalletId;
  private Long destinationWalletId;
  private String description;
  private BigDecimal amount;
  private Long createdAt;
}
