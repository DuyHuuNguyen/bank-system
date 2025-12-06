package com.bank.transaction_service.application.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ChangeOtpMessage {
  private String newOtp;
  private String personalId;
}
