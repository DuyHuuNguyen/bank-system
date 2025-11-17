package com.bank.auth_service.api.facade;

import com.bank.auth_service.api.request.*;
import com.bank.auth_service.api.response.BaseResponse;
import com.bank.auth_service.api.response.ForgotPasswordResponse;
import com.bank.auth_service.api.response.LoginResponse;
import reactor.core.publisher.Mono;

public interface AuthFacade {
  Mono<BaseResponse<LoginResponse>> login(LoginRequest loginRequest);

  Mono<BaseResponse<Void>> logout();

  Mono<BaseResponse<LoginResponse>> refresh(RefreshTokenRequest request);

  Mono<BaseResponse<ForgotPasswordResponse>> forgotPassword(ForgotPasswordRequest request);

  Mono<BaseResponse<Void>> resetPassword(ResetPasswordRequest request);

  Mono<BaseResponse<Void>> createAccount(UpsertAccountRequest request);
}
