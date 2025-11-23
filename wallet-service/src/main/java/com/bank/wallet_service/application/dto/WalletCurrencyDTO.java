package com.bank.wallet_service.application.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class WalletCurrencyDTO {
  private Long id;
  private BigDecimal availableBalance;
  private String currency;
  private Boolean isDefault;
}
