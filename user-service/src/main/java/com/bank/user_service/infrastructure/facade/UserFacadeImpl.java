package com.bank.user_service.infrastructure.facade;

import com.bank.user_service.api.facade.UserFacade;
import com.bank.user_service.api.request.ChangeUserTypeRequest;
import com.bank.user_service.api.request.CreateUserRequest;
import com.bank.user_service.api.response.BaseResponse;
import com.bank.user_service.api.response.UserDetailResponse;
import com.bank.user_service.application.dto.LocationDTO;
import com.bank.user_service.application.exception.EntityNotFoundException;
import com.bank.user_service.application.messsage.CreateAccountMessage;
import com.bank.user_service.application.service.IdentifyDocumentService;
import com.bank.user_service.application.service.PersonalInformationService;
import com.bank.user_service.application.service.ProducerCreateAccountService;
import com.bank.user_service.application.service.UserService;
import com.bank.user_service.domain.entity.IdentifyDocument;
import com.bank.user_service.domain.entity.PersonalInformation;
import com.bank.user_service.domain.entity.User;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class UserFacadeImpl implements UserFacade {
  private final UserService userService;
  private final IdentifyDocumentService identifyDocumentService;
  private final PersonalInformationService personalInformationService;
  private final ProducerCreateAccountService producerCreateAccountService;

  @Override
  @Transactional
  public Mono<BaseResponse<Void>> createUser(CreateUserRequest request) {
    return Mono.zip(this.createIdentifyDocument(request), this.createPersonalInformation(request))
        .doOnError(
            throwable -> {
              throw new EntityNotFoundException(ErrorCode.STORE_INFO_USER_ERROR);
            })
        .flatMap(
            pair -> {
              IdentifyDocument identifyDocument = pair.getT1();
              PersonalInformation personalInformation = pair.getT2();
              log.info("->>{}", identifyDocument);
              log.info("->>{}", personalInformation);
              User user =
                  User.builder()
                      .userType(request.getUserType())
                      .identifyDocumentId(identifyDocument.getId())
                      .personalInformationId(personalInformation.getId())
                      .build();
              CreateAccountMessage createAccountMessage =
                  CreateAccountMessage.builder()
                      .userId(null)
                      .email(request.getEmail())
                      .phone(request.getPhone())
                      .personalId(request.getPersonalId())
                      .password(request.getPassword())
                      .build();
              return this.userService
                  .save(user)
                  .switchIfEmpty(Mono.error(new EntityNotFoundException(ErrorCode.USER_NOT_FOUND)))
                  .map(
                      userStored ->
                          createAccountMessage.addUserIdAndReceiveMessage(userStored.getId()))
                  .flatMap(this.producerCreateAccountService::onCreateAccountMessage)
                  .thenReturn(BaseResponse.ok());
            });
  }

  @Override
  public Mono<BaseResponse<UserDetailResponse>> findUserDetailById(Long id) {
    return findUserById(id);
  }

  @Override
  public Mono<BaseResponse<UserDetailResponse>> findProfile() {
    return ReactiveSecurityContextHolder.getContext()
        .map(SecurityContext::getAuthentication)
        .map(Authentication::getPrincipal)
        .cast(SecurityUserDetails.class)
        .flatMap(principal -> this.findUserById(principal.getUserId()));
  }

  @Override
  public Mono<BaseResponse<Void>> changeUserType(ChangeUserTypeRequest request) {
    return this.userService
        .findById(request.getId())
        .switchIfEmpty(Mono.error(new EntityNotFoundException(ErrorCode.USER_NOT_FOUND)))
        .flatMap(
            user -> {
              user.changeUserType(request.getUserType());
              return this.userService
                  .save(user)
                  .switchIfEmpty(
                      Mono.error(new EntityNotFoundException(ErrorCode.STORE_INFO_USER_ERROR)))
                  .thenReturn(BaseResponse.ok());
            });
  }

  private Mono<BaseResponse<UserDetailResponse>> findUserById(Long id) {
    return this.userService
        .findById(id)
        .switchIfEmpty(Mono.error(new EntityNotFoundException(ErrorCode.USER_NOT_FOUND)))
        .doOnSuccess(ok -> log.info("Find user"))
        .doOnError(err -> log.error("Find error", err))
        .flatMap(
            user -> {
              Mono<PersonalInformation> personalInformationMono =
                  this.personalInformationService
                      .findById(user.getPersonalInformationId())
                      .switchIfEmpty(
                          Mono.error(
                              new EntityNotFoundException(
                                  ErrorCode.PERSONAL_INFORMATION_NOT_FOUND)))
                      .doOnSuccess(ok -> log.info("Personal"));
              Mono<IdentifyDocument> identifyDocumentMono =
                  this.identifyDocumentService
                      .findById(user.getIdentifyDocumentId())
                      .switchIfEmpty(
                          Mono.error(
                              new EntityNotFoundException(ErrorCode.IDENTIFY_DOCUMENT_NOT_FOUND)))
                      .doOnSuccess(ok -> log.info("Identify"));
              return Mono.zip(identifyDocumentMono, personalInformationMono)
                  .map(
                      pair -> {
                        IdentifyDocument identifyDocument = pair.getT1();
                        LocationDTO locationOfIdentifyDocument =
                            LocationDTO.builder()
                                .country(identifyDocument.getCountry())
                                .province(identifyDocument.getProvince())
                                .district(identifyDocument.getDistrict())
                                .ward(identifyDocument.getWard())
                                .street(identifyDocument.getStreet())
                                .homesNumber(identifyDocument.getHomesNumber())
                                .build();
                        PersonalInformation personalInformation = pair.getT2();
                        LocationDTO locationOfUser =
                            LocationDTO.builder()
                                .country(personalInformation.getCountry())
                                .province(personalInformation.getProvince())
                                .district(personalInformation.getDistrict())
                                .ward(personalInformation.getWard())
                                .street(personalInformation.getStreet())
                                .homesNumber(personalInformation.getHomesNumber())
                                .build();

                        return BaseResponse.build(
                            UserDetailResponse.builder()
                                .id(user.getId())
                                .personalId(identifyDocument.getPersonalId())
                                .issuesAt(identifyDocument.getIssuedAt())
                                .citizenIdFront(identifyDocument.getCitizenIdFront())
                                .citizenIdBack(identifyDocument.getCitizenIdBack())
                                .locationOfIdentifyDocument(locationOfIdentifyDocument)
                                .firstName(personalInformation.getFirstName())
                                .lastName(personalInformation.getLastName())
                                .dateOfBirth(personalInformation.getDateOfBirth())
                                .gender(personalInformation.getGender())
                                .locationOfUser(locationOfUser)
                                .build(),
                            true);
                      });
            });
  }

  private Mono<PersonalInformation> createPersonalInformation(CreateUserRequest request) {
    PersonalInformation personalInformation =
        PersonalInformation.builder()
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .dateOfBirth(request.getDateOfBirth())
            .gender(request.getGender())
            .personalPhoto(request.getPersonalPhoto())
            .build();
    return this.personalInformationService
        .save(personalInformation)
        .switchIfEmpty(Mono.error(new EntityNotFoundException(ErrorCode.PERSON_NOT_FOUND)));
  }

  private Mono<IdentifyDocument> createIdentifyDocument(CreateUserRequest request) {
    IdentifyDocument identifyDocument =
        IdentifyDocument.builder()
            .personalId(request.getPersonalId())
            .issuedAt(request.getIssuedAt())
            .citizenIdFront(request.getCitizenIdFront())
            .citizenIdBack(request.getCitizenIdBack())
            .build();
    return this.identifyDocumentService
        .save(identifyDocument)
        .switchIfEmpty(
            Mono.error(new EntityNotFoundException(ErrorCode.IDENTITY_DOCUMENT_NOT_FOUND)));
  }
}
