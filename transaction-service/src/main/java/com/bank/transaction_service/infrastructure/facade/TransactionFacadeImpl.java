package com.bank.transaction_service.infrastructure.facade;

import com.bank.transaction_service.api.facade.TransactionFacade;
import com.bank.transaction_service.api.request.CreateTransactionRequest;
import com.bank.transaction_service.api.response.BaseResponse;
import com.bank.transaction_service.application.message.TransactionMessage;
import com.bank.transaction_service.application.service.IdempotencyService;
import com.bank.transaction_service.application.service.ProducerHandleTransactionService;
import com.bank.transaction_service.infrastructure.security.SecurityUserDetails;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionFacadeImpl implements TransactionFacade {
  private final ProducerHandleTransactionService producerHandleTransactionService;
  private final IdempotencyService idempotencyService;

  @Override
  public Mono<BaseResponse<Void>> handleTransaction(CreateTransactionRequest request) {
    return this.idempotencyService
        .hasKey(request.getIdempotencyKey())
        .flatMap(
            isUnIdempotency -> {
              if (!isUnIdempotency)
                return Mono.error(new RuntimeException("Idempotency check failed"));
              return ReactiveSecurityContextHolder.getContext()
                  .map(SecurityContext::getAuthentication)
                  .map(Authentication::getPrincipal)
                  .cast(SecurityUserDetails.class)
                  .flatMap(
                      principal -> {
                        TransactionMessage transactionMessage =
                            TransactionMessage.builder()
                                .paymentRouting(request.getPaymentRouting())
                                .ownerTransactionId(principal.getUserId())
                                .sourceWalletId(request.getFromWalletId())
                                .destinationWalletId(request.getToWalletId())
                                .description(request.getDescription())
                                .amount(request.getAmount())
                                .createdAt(Instant.now().toEpochMilli())
                                .build();
                        return this.producerHandleTransactionService
                            .sendMessageHandleTransaction(transactionMessage)
                            .doOnError(error -> log.info("Bug roi"))
                            .doOnSuccess(ok -> log.info("Sent to handle queue"))
                            .thenReturn(BaseResponse.ok());
                      });
            });
  }
}
