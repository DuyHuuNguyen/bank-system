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

  public void addTransactionResponses(List<TransactionResponse> transactionResponse) {
    transactionResponses.addAll(transactionResponse);
  }
}
