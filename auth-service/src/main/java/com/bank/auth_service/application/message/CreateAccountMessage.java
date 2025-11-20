package com.bank.auth_service.application.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateAccountMessage {
  private String email;
  private String password;
  private String phone;
  private String personalId;
  private Long userId;
}
