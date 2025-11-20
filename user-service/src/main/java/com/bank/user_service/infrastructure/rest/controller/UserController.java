package com.bank.user_service.infrastructure.rest.controller;

import com.bank.user_service.api.facade.UserFacade;
import com.bank.user_service.api.request.CreateUserRequest;
import com.bank.user_service.api.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

  private final UserFacade userFacade;

  @PostMapping("/sign-up")
  @ResponseStatus(HttpStatus.OK)
  @Operation(tags = "User API")
  public Mono<BaseResponse<Void>> createUser(@RequestBody CreateUserRequest request) {
    return this.userFacade.createUser(request);
  }
}
