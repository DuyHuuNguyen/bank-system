package com.bank.wallet_service.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateWalletRequest {
  private Long userId;
  private Long currencyId;
}
