package com.bank.wallet_service.api.response;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class WalletResponse {
  private Long id;
  private BigDecimal availableBalance;
  private String currency;
  private Boolean isDefault;
}
