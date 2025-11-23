package com.bank.wallet_service.infrastructure.facade;

import com.bank.wallet_service.api.facade.CurrencyFacade;
import com.bank.wallet_service.api.request.UpsertCurrencyRequest;
import com.bank.wallet_service.api.response.BaseResponse;
import com.bank.wallet_service.api.response.CurrencyResponse;
import com.bank.wallet_service.api.response.PaginationResponse;
import com.bank.wallet_service.application.exception.EntityNotFoundException;
import com.bank.wallet_service.application.service.CurrencyService;
import com.bank.wallet_service.domain.entity.Currency;
import com.bank.wallet_service.infrastructure.enums.ErrorCode;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CurrencyFacadeImpl implements CurrencyFacade {
  private final CurrencyService currencyService;

  @Override
  @Transactional
  public Mono<BaseResponse<Void>> createCurrency(UpsertCurrencyRequest request) {
    Currency currency =
        Currency.builder()
            .currencyName(request.getCurrencyName())
            .isActive(request.getIsActive())
            .build();
    return this.currencyService.save(currency).thenReturn(BaseResponse.ok());
  }

  @Override
  @Transactional
  public Mono<BaseResponse<Void>> updateCurrency(UpsertCurrencyRequest request) {
    return this.currencyService
        .findById(request.getId())
        .switchIfEmpty(Mono.error(new EntityNotFoundException(ErrorCode.CURRENCY_NOT_FOUND)))
        .flatMap(
            currency -> {
              currency.changeInfo(request);
              return this.currencyService.save(currency);
            })
        .thenReturn(BaseResponse.ok());
  }

  @Override
  public Mono<BaseResponse<PaginationResponse<CurrencyResponse>>> findAll(
      Integer pageSize, Integer currentPage) {
    return this.currencyService
        .findAll(
            Optional.ofNullable(currentPage).orElse(0), Optional.ofNullable(pageSize).orElse(0))
        .switchIfEmpty(Mono.error(new EntityNotFoundException(ErrorCode.CURRENCY_NOT_FOUND)))
        .collectList()
        .map(
            currencies ->
                currencies.stream()
                    .map(
                        currency ->
                            CurrencyResponse.builder()
                                .id(currency.getId())
                                .currencyName(currency.getCurrencyName())
                                .isActive(currency.isActive())
                                .createdAt(currency.getCreatedAt())
                                .updatedAt(currency.getUpdatedAt())
                                .build())
                    .toList())
        .map(
            currencyResponses ->
                BaseResponse.build(
                    PaginationResponse.<CurrencyResponse>builder()
                        .pageSize(pageSize)
                        .currentPage(currentPage)
                        .data(currencyResponses)
                        .build(),
                    true));
  }

  @Override
  public Mono<BaseResponse<CurrencyResponse>> findById(Long id) {
    return this.currencyService
        .findById(id)
        .switchIfEmpty(Mono.error(new EntityNotFoundException(ErrorCode.CURRENCY_NOT_FOUND)))
        .map(
            currency ->
                BaseResponse.build(
                    CurrencyResponse.builder()
                        .id(currency.getId())
                        .currencyName(currency.getCurrencyName())
                        .isActive(currency.isActive())
                        .createdAt(currency.getCreatedAt())
                        .updatedAt(currency.getUpdatedAt())
                        .build(),
                    true));
  }
}
