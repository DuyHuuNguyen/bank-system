package com.bank.wallet_service.infrastructure.facade;

import com.bank.wallet_service.api.facade.WalletFacade;
import com.bank.wallet_service.api.request.CreateWalletRequest;
import com.bank.wallet_service.api.request.TransferRequest;
import com.bank.wallet_service.api.request.TransferResponse;
import com.bank.wallet_service.api.response.BaseResponse;
import com.bank.wallet_service.api.response.WalletResponse;
import com.bank.wallet_service.application.exception.EntityNotFoundException;
import com.bank.wallet_service.application.service.AuthGrpcClientService;
import com.bank.wallet_service.application.service.CurrencyService;
import com.bank.wallet_service.application.service.WalletService;
import com.bank.wallet_service.domain.entity.Wallet;
import com.bank.wallet_service.infrastructure.enums.ErrorCode;
import com.bank.wallet_service.infrastructure.security.SecurityUserDetails;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class WalletFacadeImpl implements WalletFacade {
  private final WalletService walletService;
  private final CurrencyService currencyService;
  private final AuthGrpcClientService authGrpcClientService;

  @Override
  @Transactional
  public Mono<BaseResponse<Void>> createWallet(CreateWalletRequest request) {
    return this.authGrpcClientService
        .findAccountByUserId(request.getUserId())
        .doOnError(
            throwable -> {
              throw new EntityNotFoundException(ErrorCode.WALLET_NOT_FOUND);
            })
        .flatMap(
            authResponse ->
                this.currencyService
                    .findById(request.getCurrencyId())
                    .switchIfEmpty(
                        Mono.error(new EntityNotFoundException(ErrorCode.CURRENCY_NOT_FOUND)))
                    .flatMap(
                        currency -> {
                          Wallet wallet =
                              Wallet.builder()
                                  .userId(request.getUserId())
                                  .currencyId(currency.getId())
                                  .isDefault(false)
                                  .build();
                          return this.walletService.save(wallet);
                        }))
        .thenReturn(BaseResponse.ok());
  }

  @Override
  public Mono<BaseResponse<List<WalletResponse>>> findMyWallets() {
    return ReactiveSecurityContextHolder.getContext()
        .map(SecurityContext::getAuthentication)
        .map(Authentication::getPrincipal)
        .cast(SecurityUserDetails.class)
        .flatMapMany(
            principal -> this.walletService.findWalletCurrencyByUserId(principal.getUserId()))
        .collectList()
        .map(
            walletCurrencyDTOS ->
                walletCurrencyDTOS.stream()
                    .map(
                        walletCurrencyDTO ->
                            WalletResponse.builder()
                                .id(walletCurrencyDTO.getId())
                                .currency(walletCurrencyDTO.getCurrencyName())
                                .availableBalance(walletCurrencyDTO.getAvailableBalance())
                                .isDefault(walletCurrencyDTO.getIsDefault())
                                .build())
                    .toList())
        .map(walletResponses -> BaseResponse.build(walletResponses, true));
  }

  @Override
  @Transactional
  public Mono<TransferResponse> transfer(TransferRequest request) {
    return this.walletService
        .transfer(
            request.getSourceWalletId(),
            request.getSourceVersion(),
            request.getDestinationWalletId(),
            request.getDestinationVersion(),
            request.getAmount())
        .thenReturn(TransferResponse.builder().isSuccess(true).build())
        .onErrorResume(exception -> Mono.just(TransferResponse.builder().isSuccess(false).build()));
  }
}
