package com.bank.user_service.infrastructure.rest.controller;

import com.bank.user_service.api.facade.UserFacade;
import com.bank.user_service.api.request.ChangeUserTypeRequest;
import com.bank.user_service.api.request.CreateUserRequest;
import com.bank.user_service.api.response.BaseResponse;
import com.bank.user_service.api.response.UserDetailResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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

  @GetMapping("/user-detail/{id}")
  @ResponseStatus(HttpStatus.OK)
  @Operation(tags = "User API")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @SecurityRequirement(name = "Bearer Authentication")
  public Mono<BaseResponse<UserDetailResponse>> findUserDetailById(@PathVariable Long id) {
    return this.userFacade.findUserDetailById(id);
  }

  @GetMapping("/profile")
  @ResponseStatus(HttpStatus.OK)
  @Operation(tags = "User API")
  @PreAuthorize("isAuthenticated()")
  @SecurityRequirement(name = "Bearer Authentication")
  public Mono<BaseResponse<UserDetailResponse>> findProfile() {
    return this.userFacade.findProfile();
  }

  @PatchMapping("/type/{id}")
  @ResponseStatus(HttpStatus.OK)
  @Operation(tags = "User API")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @SecurityRequirement(name = "Bearer Authentication")
  public Mono<BaseResponse<Void>> changeUserType(
      @PathVariable Long id, @RequestBody ChangeUserTypeRequest request) {
    request.withId(id);
    return this.userFacade.changeUserType(request);
  }
}
