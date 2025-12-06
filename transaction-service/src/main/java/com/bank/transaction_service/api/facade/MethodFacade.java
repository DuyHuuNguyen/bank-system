package com.bank.transaction_service.api.facade;

import com.bank.transaction_service.api.request.ActiveMethodRequest;
import com.bank.transaction_service.api.request.BaseCriteria;
import com.bank.transaction_service.api.request.UpsertMethodRequest;
import com.bank.transaction_service.api.response.BaseResponse;
import com.bank.transaction_service.api.response.MethodResponse;
import com.bank.transaction_service.api.response.PaginationResponse;
import reactor.core.publisher.Mono;

public interface MethodFacade {
  Mono<BaseResponse<Void>> createMethod(UpsertMethodRequest request);

  Mono<BaseResponse<Void>> changeMethodName(UpsertMethodRequest request);

  Mono<BaseResponse<Void>> changeMethodActive(ActiveMethodRequest request);

  Mono<BaseResponse<PaginationResponse<MethodResponse>>> findAll(BaseCriteria baseCriteria);
}
