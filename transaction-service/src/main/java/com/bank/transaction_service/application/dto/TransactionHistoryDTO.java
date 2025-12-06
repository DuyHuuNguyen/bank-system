package com.bank.transaction_service.application.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Getter
public class TransactionHistoryDTO {
  private Long id;
  private String transactionBalance;
  private Long sourceWalletId;
  private Long destinationWalletId;
  private String description;
  private Long createdAt;
  private String methodName;
  private String status;
  private String type;
  private String referenceCode;
  private String currency;
}
