package com.bank.transaction_service.infrastructure.facade;

import com.bank.transaction_service.api.facade.TransactionFacade;
import com.bank.transaction_service.api.request.CreateTransactionRequest;
import com.bank.transaction_service.api.request.TransactionCriteria;
import com.bank.transaction_service.api.request.TransactionDetailRequest;
import com.bank.transaction_service.api.response.BaseResponse;
import com.bank.transaction_service.api.response.PaginationResponse;
import com.bank.transaction_service.api.response.TransactionDetailResponse;
import com.bank.transaction_service.api.response.TransactionResponse;
import com.bank.transaction_service.application.dto.TransactionHistoryDTO;
import com.bank.transaction_service.application.dto.WalletDTO;
import com.bank.transaction_service.application.exception.EntityNotFoundException;
import com.bank.transaction_service.application.exception.IdempotencyException;
import com.bank.transaction_service.application.message.TransactionMessage;
import com.bank.transaction_service.application.service.IdempotencyService;
import com.bank.transaction_service.application.service.ProducerHandleTransactionService;
import com.bank.transaction_service.application.service.TransactionService;
import com.bank.transaction_service.application.service.WalletGrpcClientService;
import com.bank.transaction_service.infrastructure.enums.ErrorCode;
import com.bank.transaction_service.infrastructure.security.SecurityUserDetails;
import com.example.server.wallet.WalletOwnerRequest;
import com.example.server.wallet.WalletProfileRequest;
import com.example.server.wallet.WalletProfileResponse;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionFacadeImpl implements TransactionFacade {
  private final ProducerHandleTransactionService producerHandleTransactionService;
  private final IdempotencyService idempotencyService;
  private final TransactionService transactionService;
  private final WalletGrpcClientService walletGrpcClientService;

  @Override
  public Mono<BaseResponse<Void>> handleTransaction(CreateTransactionRequest request) {
    log.info("request {} ", request);
    return this.idempotencyService
        .hasKey(request.getIdempotencyKey())
        .flatMap(
            isUnIdempotency -> {
              log.info("check idempotency key {}", isUnIdempotency);
              if (!isUnIdempotency)
                return Mono.error(new IdempotencyException(ErrorCode.DUPLICATED_TRANSACTION));
              return ReactiveSecurityContextHolder.getContext()
                  .map(SecurityContext::getAuthentication)
                  .map(Authentication::getPrincipal)
                  .cast(SecurityUserDetails.class)
                  .flatMap(
                      principal -> {
                        TransactionMessage transactionMessage =
                            TransactionMessage.builder()
                                .paymentRouting(request.getPaymentRouting())
                                .ownerTransactionId(principal.getUserId())
                                .sourceWalletId(request.getFromWalletId())
                                .destinationWalletId(request.getToWalletId())
                                .description(request.getDescription())
                                .amount(request.getAmount())
                                .createdAt(Instant.now().toEpochMilli())
                                .transactionMethodEnum(request.getTransactionMethodEnum())
                                .build();
                        log.info("transactionMessage {}", principal);
                        log.info(
                            "transactionMessage {} ,{}",
                            principal.getAuthorities(),
                            principal.getUserId());
                        log.info("{}", transactionMessage);
                        return this.producerHandleTransactionService
                            .sendMessageHandleTransaction(transactionMessage)
                            .doOnError(error -> log.info("Bug roi"))
                            .doOnSuccess(ok -> log.info("Sent to handle queue"))
                            .thenReturn(BaseResponse.ok());
                      });
            });
  }

  @Override
  public Mono<BaseResponse<PaginationResponse<TransactionResponse>>> findByFilter(
      TransactionCriteria criteria) {

    criteria.addOffset();
    return this.transactionService
        .findAll(criteria)
        .collectList()
        .map(
            transactionHistoryDTOS ->
                transactionHistoryDTOS.stream()
                    .map(
                        transactionHistoryDTO ->
                            TransactionResponse.builder()
                                .id(transactionHistoryDTO.getId())
                                .balance(transactionHistoryDTO.getTransactionBalance())
                                .sourceWalletId(transactionHistoryDTO.getSourceWalletId())
                                .destinationWalletId(transactionHistoryDTO.getDestinationWalletId())
                                .description(transactionHistoryDTO.getDescription())
                                .createdAt(transactionHistoryDTO.getCreatedAt())
                                .methodName(transactionHistoryDTO.getMethodName())
                                .status(transactionHistoryDTO.getStatus())
                                .type(transactionHistoryDTO.getType())
                                .build())
                    .toList())
        .map(
            transactionResponses ->
                BaseResponse.build(
                    PaginationResponse.<TransactionResponse>builder()
                        .data(transactionResponses)
                        .currentPage(criteria.getCurrentPage())
                        .pageSize(criteria.getPageSize())
                        .build(),
                    true));
  }

  @Override
  public Mono<BaseResponse<PaginationResponse<TransactionResponse>>> findMyTransactionByFilter(
      TransactionCriteria criteria) {

    return ReactiveSecurityContextHolder.getContext()
        .map(SecurityContext::getAuthentication)
        .map(Authentication::getPrincipal)
        .cast(SecurityUserDetails.class)
        .flatMap(
            securityUserDetails -> {
              WalletOwnerRequest walletOwnerRequest =
                  WalletOwnerRequest.newBuilder()
                      .setWalletId(criteria.getSourceWalletId())
                      .setUserId(securityUserDetails.getUserId())
                      .build();
              return this.walletGrpcClientService
                  .findWalletOwnerByRequest(walletOwnerRequest)
                  .flatMap(
                      walletResponse -> {
                        boolean isValidSourceWalletId = walletResponse.getId() != 0;
                        if (!isValidSourceWalletId)
                          return Mono.error(
                              new EntityNotFoundException(ErrorCode.WALLET_NOT_FOUND));
                        criteria.addOffset();
                        return this.transactionService
                            .findAll(criteria)
                            .collectList()
                            .map(
                                transactionHistoryDTOS ->
                                    transactionHistoryDTOS.stream()
                                        .map(
                                            transactionHistoryDTO ->
                                                TransactionResponse.builder()
                                                    .id(transactionHistoryDTO.getId())
                                                    .balance(
                                                        transactionHistoryDTO
                                                            .getTransactionBalance())
                                                    .sourceWalletId(
                                                        transactionHistoryDTO.getSourceWalletId())
                                                    .destinationWalletId(
                                                        transactionHistoryDTO
                                                            .getDestinationWalletId())
                                                    .description(
                                                        transactionHistoryDTO.getDescription())
                                                    .createdAt(transactionHistoryDTO.getCreatedAt())
                                                    .methodName(
                                                        transactionHistoryDTO.getMethodName())
                                                    .status(transactionHistoryDTO.getStatus())
                                                    .type(transactionHistoryDTO.getType())
                                                    .build())
                                        .toList())
                            .map(
                                transactionResponses ->
                                    BaseResponse.build(
                                        PaginationResponse.<TransactionResponse>builder()
                                            .data(transactionResponses)
                                            .currentPage(criteria.getCurrentPage())
                                            .pageSize(criteria.getPageSize())
                                            .build(),
                                        true));
                      });
            });
  }

  @Override
  public Mono<BaseResponse<TransactionDetailResponse>> findTransactionDetailById(
      TransactionDetailRequest request) {

    return ReactiveSecurityContextHolder.getContext()
        .map(SecurityContext::getAuthentication)
        .map(Authentication::getPrincipal)
        .cast(SecurityUserDetails.class)
        .flatMap(
            securityUserDetails -> {
              WalletOwnerRequest walletOwnerRequest =
                  WalletOwnerRequest.newBuilder()
                      .setWalletId(request.getOwnerWalletId())
                      .setUserId(securityUserDetails.getUserId())
                      .build();
              return this.walletGrpcClientService
                  .findWalletOwnerByRequest(walletOwnerRequest)
                  .flatMap(
                      walletResponse -> {
                        boolean isValidOwnerWalletId = walletResponse.getId() != 0;
                        if (!isValidOwnerWalletId)
                          return Mono.error(
                              new EntityNotFoundException(ErrorCode.WALLET_NOT_FOUND));
                        WalletProfileRequest sourceWalletProfileRequest =
                            WalletProfileRequest.newBuilder()
                                .setWalletId(walletResponse.getId())
                                .build();
                        Mono<WalletProfileResponse> sourceWalletProfileMono =
                            this.walletGrpcClientService.findWalletProfileByRequest(
                                sourceWalletProfileRequest);
                        WalletProfileRequest beneficiaryWalletProfileRequest =
                            WalletProfileRequest.newBuilder()
                                .setWalletId(walletResponse.getId())
                                .build();
                        Mono<WalletProfileResponse> beneficiaryWalletProfileMono =
                            this.walletGrpcClientService.findWalletProfileByRequest(
                                beneficiaryWalletProfileRequest);
                        Mono<TransactionHistoryDTO> transactionMono =
                            this.transactionService.findTransactionHistoryById(request.getId());
                        return Mono.zip(
                                sourceWalletProfileMono,
                                beneficiaryWalletProfileMono,
                                transactionMono)
                            .flatMap(
                                sourceWalletAndBeneficiaryWalletProfileAndTransactionHistoryMono -> {
                                  WalletProfileResponse sourceWalletProfile =
                                      sourceWalletAndBeneficiaryWalletProfileAndTransactionHistoryMono
                                          .getT1();
                                  WalletProfileResponse beneficiaryWalletProfile =
                                      sourceWalletAndBeneficiaryWalletProfileAndTransactionHistoryMono
                                          .getT2();
                                  TransactionHistoryDTO transactionHistoryDTO =
                                      sourceWalletAndBeneficiaryWalletProfileAndTransactionHistoryMono
                                          .getT3();
                                  log.info(" {} ", sourceWalletProfile);
                                  log.info(" {} ", beneficiaryWalletProfile);
                                  log.info(" {} ", transactionHistoryDTO);

                                  TransactionDetailResponse transactionDetailResponse =
                                      TransactionDetailResponse.builder()
                                          .id(transactionHistoryDTO.getId())
                                          .balance(transactionHistoryDTO.getTransactionBalance())
                                          .currency(transactionHistoryDTO.getCurrency())
                                          .status(transactionHistoryDTO.getStatus())
                                          .type(transactionHistoryDTO.getType())
                                          .referenceCode(transactionHistoryDTO.getReferenceCode())
                                          .sourceWallet(
                                              WalletDTO.builder()
                                                  .id(sourceWalletProfile.getId())
                                                  .userId(sourceWalletProfile.getUserId())
                                                  .fullName(sourceWalletProfile.getFullName())
                                                  .build())
                                          .beneficiaryWallet(
                                              WalletDTO.builder()
                                                  .id(beneficiaryWalletProfile.getId())
                                                  .userId(beneficiaryWalletProfile.getUserId())
                                                  .fullName(beneficiaryWalletProfile.getFullName())
                                                  .build())
                                          .description(transactionHistoryDTO.getDescription())
                                          .build();

                                  return Mono.just(
                                      BaseResponse.build(transactionDetailResponse, true));
                                });
                      });
            });
  }
}
