package com.bank.auth_service.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RefreshTokenRequest {
  private String refreshToken;
}
