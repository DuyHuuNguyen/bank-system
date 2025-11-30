package com.bank.wallet_service.infrastructure.rest.controller;

import com.bank.wallet_service.api.facade.WalletFacade;
import com.bank.wallet_service.api.request.CreateWalletRequest;
import com.bank.wallet_service.api.request.DepositRequest;
import com.bank.wallet_service.api.request.TransferRequest;
import com.bank.wallet_service.api.request.WithDrawRequest;
import com.bank.wallet_service.api.response.*;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/wallets")
@RequiredArgsConstructor
public class WalletController {
  private final WalletFacade walletFacade;

  @PostMapping
  @ResponseStatus(HttpStatus.OK)
  @Operation(tags = {"WALLET API"})
  @SecurityRequirement(name = "Bearer Authentication")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public Mono<BaseResponse<Void>> createWallet(@RequestBody CreateWalletRequest request) {
    return this.walletFacade.createWallet(request);
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  @Operation(tags = {"WALLET API"})
  @SecurityRequirement(name = "Bearer Authentication")
  @PreAuthorize("isAuthenticated()")
  public Mono<BaseResponse<List<WalletResponse>>> findMyWallets() {
    return this.walletFacade.findMyWallets();
  }

  @Hidden
  @PostMapping(value = "/internal/transfer", headers = "secret-api-key=transfer-23130075")
  public Mono<TransferResponse> transfer(@RequestBody TransferRequest request) {
    return this.walletFacade.transfer(request);
  }

  @Hidden
  @PostMapping(value = "/internal/deposit", headers = "secret-api-key=transfer-23130075")
  public Mono<DepositResponse> deposit(@RequestBody DepositRequest request) {
    return this.walletFacade.deposit(request);
  }

  @Hidden
  @PostMapping(value = "/internal/withdraw", headers = "secret-api-key=transfer-23130075")
  public Mono<WithDrawResponse> withDraw(@RequestBody WithDrawRequest request) {
    return this.walletFacade.withDraw(request);
  }
}
