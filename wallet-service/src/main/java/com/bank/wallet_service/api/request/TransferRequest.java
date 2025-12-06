package com.bank.wallet_service.api.request;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TransferRequest {
  private Long sourceWalletId;
  private Long sourceVersion;
  private Long destinationWalletId;
  private Long destinationVersion;
  private BigDecimal amount;
}
