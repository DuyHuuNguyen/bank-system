package com.bank.wallet_service.api.request;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class WithDrawRequest {
  private Long id;
  private BigDecimal amount;
  private Long version;
}
