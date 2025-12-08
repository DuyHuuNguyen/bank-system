package com.bank.transaction_service.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class TransactionResponse implements Comparable<TransactionResponse> {
  private Long id;
  private String balance;
  private Long sourceWalletId;
  private Long destinationWalletId;
  private String description;
  private Long createdAt;
  private String methodName;
  private String status;
  private String type;

  @Override
  public int compareTo(TransactionResponse o) {
    return (int) (this.createdAt - o.createdAt);
  }
}
