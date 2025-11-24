package com.bank.transaction_service.api.facade;

import com.bank.transaction_service.api.request.CreateTransactionRequest;
import com.bank.transaction_service.api.response.BaseResponse;
import reactor.core.publisher.Mono;

public interface TransactionFacade {
  Mono<BaseResponse<Void>> handleTransaction(CreateTransactionRequest request);
}
