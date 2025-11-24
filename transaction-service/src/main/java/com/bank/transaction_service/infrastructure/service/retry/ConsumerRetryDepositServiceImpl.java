package com.bank.transaction_service.infrastructure.service.retry;

import com.bank.transaction_service.application.message.TransactionMessage;
import com.bank.transaction_service.application.service.ConsumerRetryDepositService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ConsumerRetryDepositServiceImpl implements ConsumerRetryDepositService {
  @Override
  public Mono<Void> consumeDepositMessage(TransactionMessage transactionMessage) {
    return null;
  }
}
