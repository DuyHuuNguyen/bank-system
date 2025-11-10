package com.bank.transaction_service.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UnAuthenticationResponse {
  private String error;
  private String message;
  private Integer statusCode;
}
