package com.bank.transaction_service.api.request;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ActiveMethodRequest {
  @Hidden private Long id;
  private Boolean isActive;

  public void withId(Long id) {
    this.id = id;
  }
}
