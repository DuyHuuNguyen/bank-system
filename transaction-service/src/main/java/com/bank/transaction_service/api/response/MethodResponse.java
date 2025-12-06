package com.bank.transaction_service.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class MethodResponse {
  private Long id;
  private String methodName;
  private Boolean isActive;
}
