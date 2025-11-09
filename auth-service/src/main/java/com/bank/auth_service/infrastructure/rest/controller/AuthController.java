package com.bank.auth_service.infrastructure.rest.controller;

import com.bank.auth_service.api.facade.AuthFacade;
import com.bank.auth_service.api.request.LoginRequest;
import com.bank.auth_service.api.response.BaseResponse;
import com.bank.auth_service.api.response.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/auths")
@RequiredArgsConstructor
public class AuthController {

  private final AuthFacade authFacade;

  @PostMapping("/login")
  @ResponseStatus(HttpStatus.OK)
  @Operation(tags = {"Auths APIs"})
  public Mono<BaseResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
    return this.authFacade.login(request);
  }
}
