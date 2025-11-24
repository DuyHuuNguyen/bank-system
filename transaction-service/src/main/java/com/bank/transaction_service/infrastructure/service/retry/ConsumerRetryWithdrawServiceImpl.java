package com.bank.transaction_service.infrastructure.service.retry;

import com.bank.transaction_service.application.message.TransactionMessage;
import com.bank.transaction_service.application.service.ConsumerRetryWithdrawService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ConsumerRetryWithdrawServiceImpl implements ConsumerRetryWithdrawService {
  @Override
  public Mono<Void> consumeWithdrawMessage(TransactionMessage transactionMessage) {
    return null;
  }
}
