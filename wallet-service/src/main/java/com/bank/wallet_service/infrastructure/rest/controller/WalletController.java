package com.bank.wallet_service.infrastructure.rest.controller;

import com.bank.wallet_service.api.facade.WalletFacade;
import com.bank.wallet_service.api.request.CreateWalletRequest;
import com.bank.wallet_service.api.response.BaseResponse;
import com.bank.wallet_service.api.response.WalletResponse;
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
}
