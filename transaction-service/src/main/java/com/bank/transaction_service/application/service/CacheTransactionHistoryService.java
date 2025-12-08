package com.bank.transaction_service.application.service;

import com.bank.transaction_service.api.response.TransactionResponse;
import com.bank.transaction_service.application.dto.CacheTransactionHistoriesDTO;
import java.time.LocalDate;
import java.util.List;
import reactor.core.publisher.Mono;

public interface CacheTransactionHistoryService {
  Mono<Boolean> put(String key, CacheTransactionHistoriesDTO cacheTransactionHistoriesDTO);

  Mono<CacheTransactionHistoriesDTO> findByKey(String key);

  Mono<Boolean> hasKey(String key);

  Mono<Boolean> cacheTransaction(
      Long userId, List<TransactionResponse> transactionResponses, LocalDate created);
}
