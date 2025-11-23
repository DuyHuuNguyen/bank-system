package com.bank.transaction_service.infrastructure.service;

import com.bank.transaction_service.application.message.TransactionMessage;
import com.bank.transaction_service.application.service.ConsumerHandleTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ConsumerHandleTransactionServiceImpl implements ConsumerHandleTransactionService {
  @Value("${rabbit-config.queue-handle}")
  private String HANDLE_QUEUE;

  @Value("${rabbit-config.queue-retry}")
  private String RETRY_QUEUE;

  @Value("${rabbit-config.queue-fail}")
  private String FAIL_QUEUE;

  @Value("${rabbit-config.queue-success}")
  private String SUCCESS_QUEUE;

  @Value("${rabbit-config.exchange-handle}")
  private String HANDLE_EXCHANGE;

  @Value("${rabbit-config.exchange-retry}")
  private String RETRY_EXCHANGE;

  @Value("${rabbit-config.exchange-fail}")
  private String FAIL_EXCHANGE;

  @Value("${rabbit-config.exchange-success}")
  private String SUCCESS_EXCHANGE;

  @Value("${rabbit-config.key-handle}")
  private String HANDLE_KEY;

  @Value("${rabbit-config.key-retry}")
  private String RETRY_KEY;

  @Value("${rabbit-config.key-fail}")
  private String FAIL_KEY;

  @Value("${rabbit-config.key-success}")
  private String SUCCESS_KEY;

  @Override
  @RabbitListener(queues = "${rabbit-config.queue-handle}")
  public Mono<Void> handleTransaction(TransactionMessage transactionMessage) {
    return null;
  }
}
