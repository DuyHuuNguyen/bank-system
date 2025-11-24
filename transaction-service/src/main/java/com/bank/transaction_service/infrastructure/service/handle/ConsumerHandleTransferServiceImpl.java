package com.bank.transaction_service.infrastructure.service.handle;

import com.bank.transaction_service.application.message.TransactionMessage;
import com.bank.transaction_service.application.service.ConsumerHandleTransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ConsumerHandleTransferServiceImpl implements ConsumerHandleTransferService {
  @Override
  public Mono<Void> consumeTransferMessage(TransactionMessage transactionMessage) {
    return null;
  }
}
