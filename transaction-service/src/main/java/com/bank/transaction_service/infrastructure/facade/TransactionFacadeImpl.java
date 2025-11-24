package com.bank.transaction_service.infrastructure.facade;

import com.bank.transaction_service.api.facade.TransactionFacade;
import com.bank.transaction_service.api.request.CreateTransactionRequest;
import com.bank.transaction_service.api.response.BaseResponse;
import com.bank.transaction_service.application.service.IdempotencyService;
import com.bank.transaction_service.application.service.ProducerHandleTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TransactionFacadeImpl implements TransactionFacade {
    private final ProducerHandleTransactionService producerHandleTransactionService;
    private final IdempotencyService idempotencyService;


    @Override
    public Mono<BaseResponse<Void>> handleTransaction(CreateTransactionRequest request) {
        return this.idempotencyService.hasKey(request.getIdempotencyKey())
                .flatMap(isUnIdempotency ->{
                        if (!isUnIdempotency)
                            return Mono.error(new RuntimeException("Idempotency check failed"));
                        return null;
                });
    }

}
