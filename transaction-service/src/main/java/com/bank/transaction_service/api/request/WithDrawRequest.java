package com.bank.transaction_service.api.request;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class WithDrawRequest {
  private Long id;
  private BigDecimal amount;
  private Long version;
}
