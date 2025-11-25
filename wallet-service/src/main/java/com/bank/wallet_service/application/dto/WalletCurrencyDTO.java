package com.bank.wallet_service.application.dto;

import java.math.BigDecimal;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Builder
public class WalletCurrencyDTO {
  private Long id;
  private BigDecimal availableBalance;
  private Boolean isDefault;
  private String currencyName;
  private Long userId;
  private Long version;
}
