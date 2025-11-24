package com.bank.transaction_service.infrastructure.service.handle;

import com.bank.transaction_service.application.message.TransactionMessage;
import com.bank.transaction_service.application.service.ConsumerHandleDepositService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ConsumerHandleDepositServiceImpl implements ConsumerHandleDepositService {
  @Override
  public Mono<Void> consumeDepositMessage(TransactionMessage transactionMessage) {
    return null;
  }
}
