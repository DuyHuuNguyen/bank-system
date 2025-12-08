package com.bank.transaction_service.application.dto;

import com.bank.transaction_service.api.response.TransactionResponse;
import java.util.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Getter
public class CacheTransactionHistoriesDTO {
  @Builder.Default private TreeSet<TransactionResponse> transactionResponses = new TreeSet<>();

  public void addTransactionResponses(List<TransactionResponse> transactionResponses) {
    this.transactionResponses.addAll(transactionResponses);
  }

  public void addTransactionResponse(TransactionResponse transactionResponse) {
    this.transactionResponses.add(transactionResponse);
  }
}
