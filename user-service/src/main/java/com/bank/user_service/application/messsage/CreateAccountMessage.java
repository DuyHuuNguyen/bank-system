package com.bank.user_service.application.messsage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CreateAccountMessage {
  private String email;
  private String password;
  private String phone;
  private String personalId;
  private Long userId;

  public CreateAccountMessage addUserIdAndReceiveMessage(Long userId) {
    this.userId = userId;
    return this;
  }
}
