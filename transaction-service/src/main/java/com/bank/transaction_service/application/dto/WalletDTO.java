package com.bank.transaction_service.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class WalletDTO {
  private Long id;
  private Long userId;
  private String fullName;
}
