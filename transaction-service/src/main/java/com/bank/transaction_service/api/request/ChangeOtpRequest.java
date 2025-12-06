package com.bank.transaction_service.api.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@NotNull
public class ChangeOtpRequest {
  private String oldOtp;
  private String newOtp;
  private String personalId;
  private String password;
}
