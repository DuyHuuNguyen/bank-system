package com.bank.transaction_service.api.request;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UpsertMethodRequest {
  @Hidden private Long id;
  @NotNull private String name;

  public void withId(Long id) {
    this.id = id;
  }
}
