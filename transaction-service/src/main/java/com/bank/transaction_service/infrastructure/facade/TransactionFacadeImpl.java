package com.bank.transaction_service.infrastructure.facade;

import com.bank.transaction_service.api.facade.TransactionFacade;
import com.bank.transaction_service.api.request.*;
import com.bank.transaction_service.api.response.BaseResponse;
import com.bank.transaction_service.api.response.PaginationResponse;
import com.bank.transaction_service.api.response.TransactionDetailResponse;
import com.bank.transaction_service.api.response.TransactionResponse;
import com.bank.transaction_service.application.dto.TransactionHistoryDTO;
import com.bank.transaction_service.application.dto.WalletDTO;
import com.bank.transaction_service.application.exception.EntityNotFoundException;
import com.bank.transaction_service.application.exception.IdempotencyException;
import com.bank.transaction_service.application.exception.OtpDontMatchingException;
import com.bank.transaction_service.application.exception.PersonalIdNotMatchException;
import com.bank.transaction_service.application.message.ChangeOtpMessage;
import com.bank.transaction_service.application.message.TransactionMessage;
import com.bank.transaction_service.application.service.*;
import com.bank.transaction_service.infrastructure.enums.ErrorCode;
import com.bank.transaction_service.infrastructure.enums.TransactionHistoryTemplate;
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
  private final ProducerChangeOtpService producerChangeOtpService;
  private final CacheTransactionHistoryService cacheTransactionHistoryService;

  @Override
  public Mono<BaseResponse<Void>> handleTransaction(CreateTransactionRequest request) {
    log.info("request {} ", request);
    return this.idempotencyService
        .hasKey(request.getIdempotencyKey())
        .flatMap(
            isUnIdempotency -> {
              log.info("check idempotency key {}", isUnIdempotency);
              if (isUnIdempotency)
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
              boolean isOnlyPaginationWithCreatedAt =
                  criteria.getTransactionCreatedAt() != null
                      && criteria.getTransactionCreatedAroundMonthAt() == null;
              if (isOnlyPaginationWithCreatedAt) {
                String transactionHistoryKey =
                    String.format(
                        TransactionHistoryTemplate.TRANSACTION_HISTORY_TEMPLATE_KEY.getContent(),
                        criteria.getSourceWalletId(),
                        criteria.getTransactionCreatedAt());
                log.info("key : {}", transactionHistoryKey);
                return this.cacheTransactionHistoryService
                    .hasKey(transactionHistoryKey)
                    //                          .doOnSuccess(ok-> log.info("Data at redis"))
                    .flatMap(
                        isHasTransactions -> {
                          log.info("boolean cache data {}", isHasTransactions);
                          if (isHasTransactions) {
                            log.info("Data at redis");
                            return this.cacheTransactionHistoryService
                                .findByKey(transactionHistoryKey)
                                .map(
                                    cacheTransactionHistoriesDTO ->
                                        BaseResponse.build(
                                            PaginationResponse.<TransactionResponse>builder()
                                                .data(
                                                    cacheTransactionHistoriesDTO
                                                        .getTransactionResponses().stream()
                                                        .toList())
                                                .currentPage(criteria.getCurrentPage())
                                                .pageSize(criteria.getPageSize())
                                                .build(),
                                            true));
                          } else {
                            log.info("chuaw cache data");
                            return this.buildTransactionResponse(
                                criteria, walletOwnerRequest, securityUserDetails);
                          }
                        });
              }
              return this.buildTransactionResponse(
                  criteria, walletOwnerRequest, securityUserDetails);
              //              return this.walletGrpcClientService
              //                  .findWalletOwnerByRequest(walletOwnerRequest)
              //                  .flatMap(
              //                      walletResponse -> {
              //                        boolean isValidSourceWalletId = walletResponse.getId() != 0;
              //                        if (!isValidSourceWalletId)
              //                          return Mono.error(
              //                              new
              // EntityNotFoundException(ErrorCode.WALLET_NOT_FOUND));
              //                        criteria.addOffset();
              //                        return this.transactionService
              //                            .findAll(criteria)
              //                            .collectList()
              //                            .map(
              //                                transactionHistoryDTOS ->
              //                                    transactionHistoryDTOS.stream()
              //                                        .map(
              //                                            transactionHistoryDTO ->
              //                                                TransactionResponse.builder()
              //
              // .id(transactionHistoryDTO.getId())
              //                                                    .balance(
              //                                                        transactionHistoryDTO
              //
              // .getTransactionBalance())
              //                                                    .sourceWalletId(
              //
              // transactionHistoryDTO.getSourceWalletId())
              //                                                    .destinationWalletId(
              //                                                        transactionHistoryDTO
              //
              // .getDestinationWalletId())
              //                                                    .description(
              //
              // transactionHistoryDTO.getDescription())
              //
              // .createdAt(transactionHistoryDTO.getCreatedAt())
              //                                                    .methodName(
              //
              // transactionHistoryDTO.getMethodName())
              //
              // .status(transactionHistoryDTO.getStatus())
              //
              // .type(transactionHistoryDTO.getType())
              //                                                    .build())
              //                                        .toList())
              //                            .flatMap(
              //                                transactionResponses -> {
              //                                   return
              // this.cacheTransactionHistoryService.cacheTransaction(securityUserDetails.getUserId(), transactionResponses,criteria.getTransactionCreatedAt())
              //
              // .thenReturn(BaseResponse.build(PaginationResponse.<TransactionResponse>builder()
              //                                                    .data(transactionResponses)
              //
              // .currentPage(criteria.getCurrentPage())
              //
              // .pageSize(criteria.getPageSize())
              //                                                    .build(),
              //                                            true));
              //                                });
              //                      });
            });
  }

  private Mono<BaseResponse<PaginationResponse<TransactionResponse>>> buildTransactionResponse(
      TransactionCriteria criteria,
      WalletOwnerRequest walletOwnerRequest,
      SecurityUserDetails securityUserDetails) {
    return this.walletGrpcClientService
        .findWalletOwnerByRequest(walletOwnerRequest)
        .flatMap(
            walletResponse -> {
              boolean isValidSourceWalletId = walletResponse.getId() != 0;
              if (!isValidSourceWalletId)
                return Mono.error(new EntityNotFoundException(ErrorCode.WALLET_NOT_FOUND));
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
                                          .destinationWalletId(
                                              transactionHistoryDTO.getDestinationWalletId())
                                          .description(transactionHistoryDTO.getDescription())
                                          .createdAt(transactionHistoryDTO.getCreatedAt())
                                          .methodName(transactionHistoryDTO.getMethodName())
                                          .status(transactionHistoryDTO.getStatus())
                                          .type(transactionHistoryDTO.getType())
                                          .build())
                              .toList())
                  .flatMap(
                      transactionResponses -> {
                        return this.cacheTransactionHistoryService
                            .cacheTransaction(
                                criteria.getSourceWalletId(),
                                transactionResponses,
                                criteria.getTransactionCreatedAt())
                            .doOnError(error -> log.info(" error {} ", error))
                            .doOnSuccess(v -> log.info(" ok cache done "))
                            .then(
                                Mono.just(
                                    BaseResponse.build(
                                        PaginationResponse.<TransactionResponse>builder()
                                            .data(transactionResponses)
                                            .currentPage(criteria.getCurrentPage())
                                            .pageSize(criteria.getPageSize())
                                            .build(),
                                        true)));
                      });
            });
  }

  @Override
  public Mono<BaseResponse<TransactionDetailResponse>> findTransactionDetail(
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

  @Override
  public Mono<BaseResponse<TransactionDetailResponse>> findTransactionDetailById(Long id) {
    return this.transactionService
        .findTransactionHistoryById(id)
        .switchIfEmpty(Mono.error(new EntityNotFoundException(ErrorCode.TRANSACTION_NOT_FOUND)))
        .flatMap(
            transactionHistoryDTO -> {
              WalletProfileRequest sourceWalletProfileRequest =
                  WalletProfileRequest.newBuilder()
                      .setWalletId(transactionHistoryDTO.getSourceWalletId())
                      .build();
              Mono<WalletProfileResponse> sourceWalletProfileMono =
                  this.walletGrpcClientService.findWalletProfileByRequest(
                      sourceWalletProfileRequest);
              WalletProfileRequest beneficiaryWalletProfileRequest =
                  WalletProfileRequest.newBuilder()
                      .setWalletId(transactionHistoryDTO.getDestinationWalletId())
                      .build();
              Mono<WalletProfileResponse> beneficiaryWalletProfileMono =
                  this.walletGrpcClientService.findWalletProfileByRequest(
                      beneficiaryWalletProfileRequest);
              return Mono.zip(sourceWalletProfileMono, beneficiaryWalletProfileMono)
                  .flatMap(
                      sourceWalletAndBeneficiaryWalletProfile -> {
                        WalletProfileResponse sourceWalletProfile =
                            sourceWalletAndBeneficiaryWalletProfile.getT1();
                        WalletProfileResponse beneficiaryWalletProfile =
                            sourceWalletAndBeneficiaryWalletProfile.getT2();
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
                        return Mono.just(BaseResponse.build(transactionDetailResponse, true));
                      });
            });
  }

  @Override
  public Mono<BaseResponse<Void>> verifyTransactionOtp(VerifyTransactionOtpRequest request) {
    return ReactiveSecurityContextHolder.getContext()
        .map(SecurityContext::getAuthentication)
        .map(Authentication::getPrincipal)
        .cast(SecurityUserDetails.class)
        .flatMap(
            securityUserDetails -> {
              boolean isValidOtp = securityUserDetails.getOtp().equals(request.getOtp());
              if (!isValidOtp) return Mono.just(BaseResponse.fail());
              return Mono.just(BaseResponse.ok());
            });
  }

  @Override
  public Mono<BaseResponse<Void>> changeOtp(ChangeOtpRequest request) {
    return ReactiveSecurityContextHolder.getContext()
        .map(SecurityContext::getAuthentication)
        .map(Authentication::getPrincipal)
        .cast(SecurityUserDetails.class)
        .flatMap(
            securityUserDetails -> {
              boolean isMatchingOtp = securityUserDetails.getOtp().equals(request.getOldOtp());
              boolean isValidPersonalId =
                  securityUserDetails.getPersonalId().equals(request.getPersonalId());
              if (!isMatchingOtp)
                return Mono.error(new OtpDontMatchingException(ErrorCode.OTP_NOT_MATCH));
              if (!isValidPersonalId)
                return Mono.error(new PersonalIdNotMatchException(ErrorCode.PERSONAL_ID_NOT_MATCH));
              ChangeOtpMessage changeOtpMessage =
                  ChangeOtpMessage.builder()
                      .newOtp(request.getNewOtp())
                      .personalId(request.getPersonalId())
                      .build();
              return this.producerChangeOtpService
                  .sendChangeOtpMessage(changeOtpMessage)
                  .onErrorResume(
                      error -> {
                        log.error("Can't update otp");
                        return Mono.error(
                            new EntityNotFoundException(ErrorCode.CAN_NOT_UPDATE_OTP));
                      })
                  .thenReturn(BaseResponse.ok());
            });
  }
}
