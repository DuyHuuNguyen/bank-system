package com.bank.transaction_service.infrastructure.rest.controller;

import com.bank.transaction_service.api.facade.TransactionFacade;
import com.bank.transaction_service.api.request.CreateTransactionRequest;
import com.bank.transaction_service.api.request.TransactionCriteria;
import com.bank.transaction_service.api.request.TransactionDetailRequest;
import com.bank.transaction_service.api.response.BaseResponse;
import com.bank.transaction_service.api.response.PaginationResponse;
import com.bank.transaction_service.api.response.TransactionDetailResponse;
import com.bank.transaction_service.api.response.TransactionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {
  private final TransactionFacade transactionFacade;

  @PostMapping
  @ResponseStatus(HttpStatus.OK)
  @Operation(tags = {"TRANSACTION APIs"})
  @SecurityRequirement(name = "Bearer Authentication")
  @PreAuthorize("isAuthenticated()")
  public Mono<BaseResponse<Void>> createTransaction(
      @RequestBody CreateTransactionRequest createTransactionRequest) {
    return this.transactionFacade.handleTransaction(createTransactionRequest);
  }

  @GetMapping("/managements")
  @ResponseStatus(HttpStatus.OK)
  @Operation(tags = {"TRANSACTION APIs"})
  @SecurityRequirement(name = "Bearer Authentication")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public Mono<BaseResponse<PaginationResponse<TransactionResponse>>> findByFilter(
      @NotNull TransactionCriteria criteria) {
    return this.transactionFacade.findByFilter(criteria);
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  @Operation(tags = {"TRANSACTION APIs"})
  @SecurityRequirement(name = "Bearer Authentication")
  @PreAuthorize("isAuthenticated()")
  public Mono<BaseResponse<PaginationResponse<TransactionResponse>>> findMyTransactionByFilter(
      @Valid @NotNull TransactionCriteria criteria) {
    return this.transactionFacade.findMyTransactionByFilter(criteria);
  }

  @GetMapping("/transaction-detail/{id}")
  @ResponseStatus(HttpStatus.OK)
  @Operation(tags = {"TRANSACTION APIs"})
  @SecurityRequirement(name = "Bearer Authentication")
  @PreAuthorize("isAuthenticated()")
  public Mono<BaseResponse<TransactionDetailResponse>> findDetailTransaction(
      @PathVariable Long id, @Valid @NotNull TransactionDetailRequest request) {
    request.withId(id);
    return this.transactionFacade.findTransactionDetailById(request);
  }
}
