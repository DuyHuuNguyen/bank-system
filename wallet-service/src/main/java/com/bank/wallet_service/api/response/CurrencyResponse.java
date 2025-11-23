package com.bank.wallet_service.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class CurrencyResponse {
  private Long id;
  private String currencyName;
  private Boolean isActive;
  private Long createdAt;
  private Long updatedAt;
}
