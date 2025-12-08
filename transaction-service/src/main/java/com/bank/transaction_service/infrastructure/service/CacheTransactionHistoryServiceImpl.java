package com.bank.transaction_service.infrastructure.service;

import com.bank.transaction_service.api.response.TransactionResponse;
import com.bank.transaction_service.application.dto.CacheTransactionHistoriesDTO;
import com.bank.transaction_service.application.exception.EntityNotFoundException;
import com.bank.transaction_service.application.service.CacheTransactionHistoryService;
import com.bank.transaction_service.infrastructure.enums.ErrorCode;
import com.bank.transaction_service.infrastructure.enums.TransactionHistoryTemplate;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class CacheTransactionHistoryServiceImpl implements CacheTransactionHistoryService {
  private final ReactiveRedisTemplate<String, CacheTransactionHistoriesDTO> reactiveRedisTemplate;
  private final Duration timeOut = Duration.ofDays(3);

  @Override
  public Mono<Boolean> put(String key, CacheTransactionHistoriesDTO cacheTransactionHistoriesDTO) {
    return reactiveRedisTemplate.opsForValue().set(key, cacheTransactionHistoriesDTO, timeOut);
  }

  @Override
  public Mono<CacheTransactionHistoriesDTO> findByKey(String key) {
    return reactiveRedisTemplate.opsForValue().get(key);
  }

  @Override
  public Mono<Boolean> hasKey(String key) {
    return reactiveRedisTemplate.hasKey(key);
  }

  public Mono<Boolean> cacheTransaction(
      Long walletId, List<TransactionResponse> transactionResponses, LocalDate created) {

    String transactionHistoryKey =
        String.format(
            TransactionHistoryTemplate.TRANSACTION_HISTORY_TEMPLATE_KEY.getContent(),
            walletId,
            created);
    log.info("key {}", transactionHistoryKey);
    return this.findByKey(transactionHistoryKey)
        .switchIfEmpty(Mono.error(new EntityNotFoundException(ErrorCode.CACHE_NOT_FOUND)))
        .onErrorResume(
            cacheNotFound -> {
              log.info("CACHE NOT FOUND {}", cacheNotFound.getMessage());
              return Mono.just(CacheTransactionHistoriesDTO.builder().build());
            })
        .flatMap(
            cacheTransactionHistoriesDTO -> {
              cacheTransactionHistoriesDTO.addTransactionResponses(transactionResponses);
              return this.put(transactionHistoryKey, cacheTransactionHistoriesDTO)
                  .doOnSuccess(ok -> log.info("Cached data"))
                  .doOnError(error -> log.warn("Error cache data"));
            });
  }

  @Override
  public Mono<Boolean> cacheTransaction(
      Long walletId, TransactionResponse transactionResponse, LocalDate created) {

    String transactionHistoryKey =
        String.format(
            TransactionHistoryTemplate.TRANSACTION_HISTORY_TEMPLATE_KEY.getContent(),
            walletId,
            created);
    log.info("key {}", transactionHistoryKey);
    return this.findByKey(transactionHistoryKey)
        .switchIfEmpty(Mono.error(new EntityNotFoundException(ErrorCode.CACHE_NOT_FOUND)))
        .onErrorResume(
            cacheNotFound -> {
              log.info("CACHE NOT FOUND {}", cacheNotFound.getMessage());
              return Mono.just(CacheTransactionHistoriesDTO.builder().build());
            })
        .flatMap(
            cacheTransactionHistoriesDTO -> {
              cacheTransactionHistoriesDTO.addTransactionResponse(transactionResponse);
              return this.put(transactionHistoryKey, cacheTransactionHistoriesDTO)
                  .doOnSuccess(ok -> log.info("Cached data"))
                  .doOnError(error -> log.warn("Error cache data"));
            });
  }
}
