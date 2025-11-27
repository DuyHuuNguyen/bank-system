package com.bank.transaction_service.infrastructure.service;

import com.bank.transaction_service.application.message.TransactionMessage;
import com.bank.transaction_service.application.service.ProducerSuccessTransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class ProducerSuccessTransactionServiceImpl implements ProducerSuccessTransactionService {

  @Override
  public Mono<Void> sendSuccessTransactionMessage(TransactionMessage transactionMessage) {
    log.info("Successful transaction ");
    return Mono.empty();
  }
}
