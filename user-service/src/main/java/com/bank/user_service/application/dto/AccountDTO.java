package com.bank.user_service.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {
  private Long id;
  private String email;
  private String password;
  private String phone;
  private String personalId;
  private String otp;
  private Boolean isActive;
}
