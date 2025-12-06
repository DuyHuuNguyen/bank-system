package com.bank.transaction_service.infrastructure.facade;

import com.bank.transaction_service.api.facade.MethodFacade;
import com.bank.transaction_service.api.request.ActiveMethodRequest;
import com.bank.transaction_service.api.request.BaseCriteria;
import com.bank.transaction_service.api.request.UpsertMethodRequest;
import com.bank.transaction_service.api.response.BaseResponse;
import com.bank.transaction_service.api.response.MethodResponse;
import com.bank.transaction_service.api.response.PaginationResponse;
import com.bank.transaction_service.application.exception.EntityNotFoundException;
import com.bank.transaction_service.application.service.MethodService;
import com.bank.transaction_service.domain.entity.Method;
import com.bank.transaction_service.infrastructure.enums.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class MethodFacadeImpl implements MethodFacade {
  private final MethodService methodService;

  @Override
  @Transactional
  public Mono<BaseResponse<Void>> createMethod(UpsertMethodRequest request) {
    Method method = Method.builder().methodName(request.getName()).build();
    return this.methodService
        .save(method)
        .switchIfEmpty(Mono.error(new EntityNotFoundException(ErrorCode.METHOD_NOT_FOUND)))
        .doOnSuccess(ok -> log.info("Created method"))
        .doOnError(error -> log.warn("Can't create method"))
        .thenReturn(BaseResponse.ok());
  }

  @Override
  @Transactional
  public Mono<BaseResponse<Void>> changeMethodName(UpsertMethodRequest request) {
    return this.methodService
        .findById(request.getId())
        .switchIfEmpty(Mono.error(new EntityNotFoundException(ErrorCode.METHOD_NOT_FOUND)))
        .flatMap(
            method -> {
              method.changMethodName(request.getName());
              return this.methodService
                  .save(method)
                  .switchIfEmpty(
                      Mono.error(new EntityNotFoundException(ErrorCode.METHOD_NOT_FOUND)))
                  .doOnSuccess(ok -> log.info("changed method name"))
                  .doOnError(error -> log.warn("Can't change method name"))
                  .thenReturn(BaseResponse.ok());
            });
  }

  @Override
  @Transactional
  public Mono<BaseResponse<Void>> changeMethodActive(ActiveMethodRequest request) {
    return this.methodService
        .findById(request.getId())
        .switchIfEmpty(Mono.error(new EntityNotFoundException(ErrorCode.METHOD_NOT_FOUND)))
        .flatMap(
            method -> {
              method.changeIsActive(request.getIsActive());
              return this.methodService
                  .save(method)
                  .switchIfEmpty(
                      Mono.error(new EntityNotFoundException(ErrorCode.METHOD_NOT_FOUND)))
                  .doOnSuccess(ok -> log.info("changed is active "))
                  .doOnError(error -> log.warn("Can't change active"))
                  .thenReturn(BaseResponse.ok());
            });
  }

  @Override
  public Mono<BaseResponse<PaginationResponse<MethodResponse>>> findAll(BaseCriteria baseCriteria) {
    int offset = baseCriteria.getCurrentPage() * baseCriteria.getPageSize();
    return this.methodService
        .findAll(baseCriteria.getPageSize(), offset)
        .switchIfEmpty(Mono.error(new EntityNotFoundException(ErrorCode.METHOD_NOT_FOUND)))
        .collectList()
        .map(
            methods ->
                methods.stream()
                    .map(
                        method ->
                            MethodResponse.builder()
                                .id(method.getId())
                                .methodName(method.getMethodName())
                                .isActive(method.isActive())
                                .build())
                    .toList())
        .map(
            methodResponses ->
                BaseResponse.build(
                    PaginationResponse.<MethodResponse>builder()
                        .data(methodResponses)
                        .currentPage(baseCriteria.getCurrentPage())
                        .pageSize(baseCriteria.getPageSize())
                        .build(),
                    true));
  }
}
