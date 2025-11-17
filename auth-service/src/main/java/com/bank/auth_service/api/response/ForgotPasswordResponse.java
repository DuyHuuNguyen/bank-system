package com.bank.auth_service.api.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ForgotPasswordResponse {
  private String resetPasswordToken;
}
