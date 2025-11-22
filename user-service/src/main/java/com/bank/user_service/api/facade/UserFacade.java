package com.bank.user_service.api.facade;

import com.bank.user_service.api.request.ChangeUserTypeRequest;
import com.bank.user_service.api.request.CreateUserRequest;
import com.bank.user_service.api.response.BaseResponse;
import com.bank.user_service.api.response.UserDetailResponse;
import reactor.core.publisher.Mono;

public interface UserFacade {

  Mono<BaseResponse<Void>> createUser(CreateUserRequest request);

  Mono<BaseResponse<UserDetailResponse>> findUserDetailById(Long id);

  Mono<BaseResponse<UserDetailResponse>> findProfile();

  Mono<BaseResponse<Void>> changeUserType(ChangeUserTypeRequest request);
}
