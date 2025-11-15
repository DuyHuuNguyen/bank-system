package com.bank.auth_service.api.facade;

import com.bank.auth_service.api.request.LoginRequest;
import com.bank.auth_service.api.request.RefreshTokenRequest;
import com.bank.auth_service.api.response.BaseResponse;
import com.bank.auth_service.api.response.LoginResponse;
import reactor.core.publisher.Mono;

public interface AuthFacade {
  Mono<BaseResponse<LoginResponse>> login(LoginRequest loginRequest);

  Mono<BaseResponse<Void>> logout();

  Mono<BaseResponse<LoginResponse>> refresh(RefreshTokenRequest request);
}
