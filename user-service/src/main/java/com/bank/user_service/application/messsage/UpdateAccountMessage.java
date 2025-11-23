package com.bank.user_service.application.messsage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class UpdateAccountMessage {
  private Long accountId;
  private String personalId;
}
