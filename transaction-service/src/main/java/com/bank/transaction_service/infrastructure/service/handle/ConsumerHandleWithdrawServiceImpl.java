package com.bank.transaction_service.infrastructure.service.handle;

import com.bank.transaction_service.application.message.TransactionMessage;
import com.bank.transaction_service.application.service.ConsumerHandleWithdrawService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ConsumerHandleWithdrawServiceImpl implements ConsumerHandleWithdrawService {
  @Override
  public Mono<Void> consumeDepositMessage(TransactionMessage transactionMessage) {
    return null;
  }
}
