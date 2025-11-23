package com.bank.user_service.infrastructure.facade;

import com.bank.user_service.api.facade.PersonalInformationFacade;
import com.bank.user_service.api.request.UpsertPersonalInfoRequest;
import com.bank.user_service.api.response.BaseResponse;
import com.bank.user_service.api.response.PersonalInformationResponse;
import com.bank.user_service.application.exception.EntityNotFoundException;
import com.bank.user_service.application.service.PersonalInformationService;
import com.bank.user_service.infrastructure.enums.ErrorCode;
import com.bank.user_service.infrastructure.security.SecurityUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PersonalInformationFacadeImpl implements PersonalInformationFacade {
  private final PersonalInformationService personalInformationService;

  @Override
  @Transactional
  public Mono<BaseResponse<Void>> changePersonalInfo(UpsertPersonalInfoRequest request) {
    return this.personalInformationService
        .findById(request.getId())
        .switchIfEmpty(
            Mono.error(new EntityNotFoundException(ErrorCode.PERSONAL_INFORMATION_NOT_FOUND)))
        .flatMap(
            personalInformation -> {
              personalInformation.changeInfo(request);
              return this.personalInformationService
                  .save(personalInformation)
                  .switchIfEmpty(
                      Mono.error(
                          new EntityNotFoundException(ErrorCode.PERSONAL_INFORMATION_NOT_FOUND)))
                  .thenReturn(BaseResponse.ok());
            });
  }

  @Override
  public Mono<BaseResponse<PersonalInformationResponse>> findMyInfo() {
    return ReactiveSecurityContextHolder.getContext()
        .map(SecurityContext::getAuthentication)
        .map(Authentication::getPrincipal)
        .cast(SecurityUserDetails.class)
        .flatMap(principal -> this.findPersonalInfo(principal.getUserId()));
  }

  @Override
  public Mono<BaseResponse<PersonalInformationResponse>> findPersonalInformationById(Long id) {
    return this.findPersonalInfo(id);
  }

  private Mono<BaseResponse<PersonalInformationResponse>> findPersonalInfo(Long userId) {
    return this.personalInformationService
        .findByUserId(userId)
        .switchIfEmpty(
            Mono.error(new EntityNotFoundException(ErrorCode.PERSONAL_INFORMATION_NOT_FOUND)))
        .map(
            personalInformation ->
                BaseResponse.build(
                    PersonalInformationResponse.builder()
                        .id(personalInformation.getId())
                        .firstName(personalInformation.getFirstName())
                        .lastName(personalInformation.getLastName())
                        .dateOfBirth(personalInformation.getDateOfBirth())
                        .gender(personalInformation.getGender())
                        .personalPhoto(personalInformation.getPersonalPhoto())
                        .country(personalInformation.getCountry())
                        .province(personalInformation.getProvince())
                        .district(personalInformation.getDistrict())
                        .ward(personalInformation.getWard())
                        .street(personalInformation.getStreet())
                        .homesNumber(personalInformation.getHomesNumber())
                        .build(),
                    true));
  }
}
