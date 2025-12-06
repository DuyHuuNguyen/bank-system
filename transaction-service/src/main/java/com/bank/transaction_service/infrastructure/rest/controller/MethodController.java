package com.bank.transaction_service.infrastructure.rest.controller;

import com.bank.transaction_service.api.facade.MethodFacade;
import com.bank.transaction_service.api.request.ActiveMethodRequest;
import com.bank.transaction_service.api.request.BaseCriteria;
import com.bank.transaction_service.api.request.UpsertMethodRequest;
import com.bank.transaction_service.api.response.BaseResponse;
import com.bank.transaction_service.api.response.MethodResponse;
import com.bank.transaction_service.api.response.PaginationResponse;
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
@RequestMapping("/api/v1/methods")
@RequiredArgsConstructor
public class MethodController {
  private final MethodFacade methodFacade;

  @PostMapping
  @ResponseStatus(HttpStatus.OK)
  @Operation(tags = {"METHOD APIs"})
  @SecurityRequirement(name = "Bearer Authentication")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public Mono<BaseResponse<Void>> createMethod(@RequestBody @Valid UpsertMethodRequest request) {
    return this.methodFacade.createMethod(request);
  }

  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  @Operation(tags = {"METHOD APIs"})
  @SecurityRequirement(name = "Bearer Authentication")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public Mono<BaseResponse<Void>> changeMethod(
      @PathVariable Long id, @RequestBody @Valid UpsertMethodRequest request) {
    request.withId(id);
    return this.methodFacade.changeMethodName(request);
  }

  @PatchMapping("/is-active/{id}")
  @ResponseStatus(HttpStatus.OK)
  @Operation(tags = {"METHOD APIs"})
  @SecurityRequirement(name = "Bearer Authentication")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public Mono<BaseResponse<Void>> changeMethodActive(
      @PathVariable Long id, @RequestBody @Valid ActiveMethodRequest request) {
    request.withId(id);
    return this.methodFacade.changeMethodActive(request);
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  @Operation(tags = {"METHOD APIs"})
  @SecurityRequirement(name = "Bearer Authentication")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public Mono<BaseResponse<PaginationResponse<MethodResponse>>> findAll(
      @NotNull BaseCriteria baseCriteria) {
    return this.methodFacade.findAll(baseCriteria);
  }
}
