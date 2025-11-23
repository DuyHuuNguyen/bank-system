package com.bank.user_service.infrastructure.facade;

import com.bank.user_service.api.facade.IdentifyDocumentFacade;
import com.bank.user_service.api.request.ChangeIdentifyDocumentRequest;
import com.bank.user_service.api.response.BaseResponse;
import com.bank.user_service.api.response.IdentifyDocumentResponse;
import com.bank.user_service.application.exception.EntityNotFoundException;
import com.bank.user_service.application.messsage.UpdateAccountMessage;
import com.bank.user_service.application.service.IdentifyDocumentService;
import com.bank.user_service.application.service.ProducerUpdateAccountService;
import com.bank.user_service.infrastructure.enums.ErrorCode;
import com.bank.user_service.infrastructure.security.SecurityUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class IdentifyDocumentFacadeImpl implements IdentifyDocumentFacade {
  private final IdentifyDocumentService identifyDocumentService;
  private final ProducerUpdateAccountService producerUpdateAccountService;

  @Override
  @Transactional
  public Mono<BaseResponse<Void>> changeIdentifyDocument(ChangeIdentifyDocumentRequest request) {
    return ReactiveSecurityContextHolder.getContext()
        .map(SecurityContext::getAuthentication)
        .map(Authentication::getPrincipal)
        .cast(SecurityUserDetails.class)
        .flatMap(
            principal ->
                this.identifyDocumentService
                    .findByUserId(principal.getUserId())
                    .switchIfEmpty(
                        Mono.error(
                            new EntityNotFoundException(ErrorCode.IDENTITY_DOCUMENT_NOT_FOUND)))
                    .flatMap(
                        identifyDocument -> {
                          identifyDocument.changeInfo(request);
                          return this.identifyDocumentService
                              .save(identifyDocument)
                              .switchIfEmpty(
                                  Mono.error(
                                      new EntityNotFoundException(ErrorCode.STORE_INFO_USER_ERROR)))
                              .flatMap(
                                  identifyDocumentUpdated -> {
                                    boolean isChangedPersonalId =
                                        !identifyDocument
                                            .getPersonalId()
                                            .equals(request.getPersonalId());
                                    if (!isChangedPersonalId) return Mono.just(BaseResponse.ok());
                                    UpdateAccountMessage updateAccountMessage =
                                        UpdateAccountMessage.builder()
                                            .accountId(principal.getAccountId())
                                            .personalId(request.getPersonalId())
                                            .build();
                                    return this.producerUpdateAccountService
                                        .sendMessageUpdateAccount(updateAccountMessage)
                                        .doOnSuccess(
                                            accountStored ->
                                                log.info("Account created successfully"))
                                        .doOnError(
                                            err -> log.error("Failed to create account", err))
                                        .thenReturn(BaseResponse.ok());
                                  });
                        }));
  }

  @Override
  public Mono<BaseResponse<IdentifyDocumentResponse>> findIdentifyDocumentById(Long id) {

    return ReactiveSecurityContextHolder.getContext()
        .map(SecurityContext::getAuthentication)
        .map(Authentication::getPrincipal)
        .cast(SecurityUserDetails.class)
        .flatMap(
            principal ->
                this.identifyDocumentService
                    .findByUserId(principal.getUserId())
                    .switchIfEmpty(
                        Mono.error(
                            new EntityNotFoundException(ErrorCode.IDENTITY_DOCUMENT_NOT_FOUND)))
                    .map(
                        identifyDocument ->
                            BaseResponse.build(
                                IdentifyDocumentResponse.builder()
                                    .id(identifyDocument.getId())
                                    .personalId(identifyDocument.getPersonalId())
                                    .issuedAt(identifyDocument.getIssuedAt())
                                    .citizenIdFront(identifyDocument.getCitizenIdFront())
                                    .citizenIdBack(identifyDocument.getCitizenIdBack())
                                    .country(identifyDocument.getCountry())
                                    .province(identifyDocument.getProvince())
                                    .district(identifyDocument.getDistrict())
                                    .ward(identifyDocument.getWard())
                                    .street(identifyDocument.getStreet())
                                    .homesNumber(identifyDocument.getHomesNumber())
                                    .build(),
                                true)));
  }
}
