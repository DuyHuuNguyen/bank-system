package com.bank.auth_service.api.facade;

import com.bank.auth_service.api.request.ChangeRoleAccountRequest;
import com.bank.auth_service.api.request.UpsertRoleRequest;
import com.bank.auth_service.api.response.BaseResponse;
import reactor.core.publisher.Mono;

public interface RoleFacade {
  Mono<BaseResponse<Void>> createRole(UpsertRoleRequest request);

  Mono<BaseResponse<Void>> removeRole(Long id);

  Mono<BaseResponse<Void>> changeRoleForAccount(ChangeRoleAccountRequest request);
}
