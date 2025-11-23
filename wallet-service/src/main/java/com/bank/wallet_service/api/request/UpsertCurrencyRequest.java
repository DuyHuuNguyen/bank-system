package com.bank.wallet_service.api.request;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UpsertCurrencyRequest {
  @Hidden private Long id;
  private String currencyName;
  private Boolean isActive;

  public void withId(Long id) {
    this.id = id;
  }
}
