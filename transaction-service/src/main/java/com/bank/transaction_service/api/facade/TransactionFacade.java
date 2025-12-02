package com.bank.transaction_service.api.facade;

import com.bank.transaction_service.api.request.CreateTransactionRequest;
import com.bank.transaction_service.api.request.TransactionCriteria;
import com.bank.transaction_service.api.response.BaseResponse;
import com.bank.transaction_service.api.response.PaginationResponse;
import com.bank.transaction_service.api.response.TransactionResponse;
import reactor.core.publisher.Mono;

public interface TransactionFacade {
  Mono<BaseResponse<Void>> handleTransaction(CreateTransactionRequest request);

  Mono<BaseResponse<PaginationResponse<TransactionResponse>>> findByFilter(
      TransactionCriteria criteria);

  Mono<BaseResponse<PaginationResponse<TransactionResponse>>> findMyTransactionByFilter(
      TransactionCriteria criteria);
}
