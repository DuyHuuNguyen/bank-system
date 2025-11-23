package com.bank.wallet_service.infrastructure.rest.controller;

import com.bank.wallet_service.api.facade.CurrencyFacade;
import com.bank.wallet_service.api.request.UpsertCurrencyRequest;
import com.bank.wallet_service.api.response.BaseResponse;
import com.bank.wallet_service.api.response.CurrencyResponse;
import com.bank.wallet_service.api.response.PaginationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/currencies")
@RequiredArgsConstructor
public class CurrencyController {
  private final CurrencyFacade currencyFacade;

  @PostMapping
  @ResponseStatus(HttpStatus.OK)
  @Operation(tags = {"Category APIs"})
  @SecurityRequirement(name = "Bearer Authentication")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public Mono<BaseResponse<Void>> createCurrency(@RequestBody UpsertCurrencyRequest request) {
    return this.currencyFacade.createCurrency(request);
  }

  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  @Operation(tags = {"Category APIs"})
  @SecurityRequirement(name = "Bearer Authentication")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public Mono<BaseResponse<Void>> updateCurrency(
      @PathVariable Long id, @RequestBody UpsertCurrencyRequest request) {
    request.withId(id);
    return this.currencyFacade.updateCurrency(request);
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  @Operation(tags = {"Category APIs"})
  @SecurityRequirement(name = "Bearer Authentication")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public Mono<BaseResponse<PaginationResponse<CurrencyResponse>>> findAll(
      @RequestParam("pageSize") Integer pageSize,
      @RequestParam("currentPage") Integer currentPage) {
    return this.currencyFacade.findAll(pageSize, currentPage);
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  @Operation(tags = {"Category APIs"})
  @SecurityRequirement(name = "Bearer Authentication")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public Mono<BaseResponse<CurrencyResponse>> findAll(@PathVariable Long id) {
    return this.currencyFacade.findById(id);
  }
}
