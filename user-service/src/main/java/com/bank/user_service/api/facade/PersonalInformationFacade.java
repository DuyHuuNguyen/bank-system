package com.bank.user_service.api.facade;

import com.bank.user_service.api.request.UpsertPersonalInfoRequest;
import com.bank.user_service.api.response.BaseResponse;
import com.bank.user_service.api.response.PersonalInformationResponse;
import reactor.core.publisher.Mono;

public interface PersonalInformationFacade {
  Mono<BaseResponse<Void>> changePersonalInfo(UpsertPersonalInfoRequest request);

  Mono<BaseResponse<PersonalInformationResponse>> findMyInfo();

  Mono<BaseResponse<PersonalInformationResponse>> findPersonalInformationById(Long id);
}
