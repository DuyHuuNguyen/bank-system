package com.bank.transaction_service.infrastructure.service;

import com.bank.transaction_service.api.request.TransferRequest;
import com.bank.transaction_service.api.response.TransferResponse;
import com.bank.transaction_service.application.service.WalletService;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

  private final WebClient webClient;

  @Override
  public Mono<Long> subBalanceWallet(Long id, BigDecimal amount, Long version) {
    return null;
  }

  @Override
  public Mono<Long> addBalanceWallet(Long id, BigDecimal amount, Long version) {
    return null;
  }

  @Override
  public Mono<Boolean> transfer(
      Long sourceWalletId,
      Long sourceVersion,
      Long destinationWalletId,
      Long destinationVersion,
      BigDecimal amount) {
    return WebClient.create("http://localhost:8083")
        .method(HttpMethod.POST)
        .uri("/api/v1/wallets/internal/transfer")
        .header("secret-api-key", "transfer-23130075")
        .bodyValue(
            TransferRequest.builder()
                .amount(amount)
                .sourceWalletId(sourceWalletId)
                .sourceVersion(sourceVersion)
                .destinationWalletId(destinationWalletId)
                .destinationVersion(destinationVersion)
                .build())
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .bodyToMono(TransferResponse.class)
        .map(TransferResponse::getIsSuccess)
        .doOnSuccess(ok -> log.debug("transfer completed success={}", ok))
        .doOnError(
            e ->
                log.warn(
                    "transfer failed source={} dest={} amount={} -> {}",
                    sourceWalletId,
                    destinationWalletId,
                    amount,
                    e.toString()));
  }
}
