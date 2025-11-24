package com.bank.transaction_service.infrastructure.service;

import com.bank.transaction_service.application.message.TransactionMessage;
import com.bank.transaction_service.application.service.ProducerFailTransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class ProducerFailTransactionServiceImpl implements ProducerFailTransactionService {
  @Override
  public Mono<Void> sendFailTransactionMessage(TransactionMessage transactionMessage) {
    log.info("fail:)))");
    return null;
  }
}
