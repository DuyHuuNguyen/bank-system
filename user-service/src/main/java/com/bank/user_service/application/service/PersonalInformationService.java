package com.bank.user_service.application.service;

import com.bank.user_service.api.response.BaseResponse;
import com.bank.user_service.api.response.PersonalInformationResponse;
import com.bank.user_service.domain.entity.PersonalInformation;
import reactor.core.publisher.Mono;

public interface PersonalInformationService {
  Mono<PersonalInformation> save(PersonalInformation personalInformation);

  Mono<PersonalInformation> findById(Long id);

  Mono<PersonalInformation> findByUserId(Long userId);
}
