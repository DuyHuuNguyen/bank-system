package com.bank.transaction_service.infrastructure.service.retry;

import com.bank.transaction_service.application.message.TransactionMessage;
import com.bank.transaction_service.application.service.ConsumerHandleTransferService;
import com.bank.transaction_service.application.service.ConsumerRetryTransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ConsumerRetryTransferServiceImpl implements ConsumerRetryTransferService {
    @Override
    public Mono<Void> consumeTransferMessage(TransactionMessage transactionMessage) {
        return null;
    }
}
