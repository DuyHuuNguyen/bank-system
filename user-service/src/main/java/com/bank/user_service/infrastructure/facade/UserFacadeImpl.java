package com.bank.user_service.infrastructure.facade;

import com.bank.user_service.api.facade.UserFacade;
import com.bank.user_service.api.request.CreateUserRequest;
import com.bank.user_service.api.response.BaseResponse;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        //            .doOnError( throwable ->{
        //                throw  new EntityNotFoundException(ErrorCode.IDENTITY_DOCUMENT_NOT_FOUND);
        //            })
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
                  .switchIfEmpty(Mono.error(new Exception("I have not created this")))
                  .map(
                      userStored ->
                          createAccountMessage.addUserIdAndReceiveMessage(userStored.getId()))
                  .flatMap(this.producerCreateAccountService::onCreateAccountMessage)
                  .thenReturn(BaseResponse.ok());
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

  //  @PostConstruct
  //  void run(){
  //    IdentifyDocument identifyDocument =
  //            IdentifyDocument.builder()
  //                    .personalId("request.getPersonalId()")
  //                    .createdAt(6513516l)
  //                    .updatedAt(12321321l)
  //                    .isActive(true)
  //                    .version(1l)
  //                    .build();
  //    this.identifyDocumentService
  //            .save(identifyDocument).subscribe();
  //  }
}
