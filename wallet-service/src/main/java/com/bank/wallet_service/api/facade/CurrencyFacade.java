package com.bank.wallet_service.api.facade;

import com.bank.wallet_service.api.request.UpsertCurrencyRequest;
import com.bank.wallet_service.api.response.BaseResponse;
import com.bank.wallet_service.api.response.CurrencyResponse;
import com.bank.wallet_service.api.response.PaginationResponse;
import reactor.core.publisher.Mono;

public interface CurrencyFacade {
  Mono<BaseResponse<Void>> createCurrency(UpsertCurrencyRequest request);

  Mono<BaseResponse<Void>> updateCurrency(UpsertCurrencyRequest request);

  Mono<BaseResponse<PaginationResponse<CurrencyResponse>>> findAll(
      Integer pageSize, Integer currentPage);

  Mono<BaseResponse<CurrencyResponse>> findById(Long id);
}
