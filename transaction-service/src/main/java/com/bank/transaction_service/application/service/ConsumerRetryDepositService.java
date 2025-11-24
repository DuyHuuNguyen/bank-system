package com.bank.transaction_service.application.service;

import com.bank.transaction_service.application.message.TransactionMessage;
import reactor.core.publisher.Mono;

public interface ConsumerRetryDepositService {
    Mono<Void> consumeDepositMessage(TransactionMessage transactionMessage);
}
