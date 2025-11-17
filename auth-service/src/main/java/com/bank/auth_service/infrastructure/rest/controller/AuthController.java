package com.bank.auth_service.infrastructure.rest.controller;

import com.bank.auth_service.api.facade.AuthFacade;
import com.bank.auth_service.api.request.*;
import com.bank.auth_service.api.response.BaseResponse;
import com.bank.auth_service.api.response.ForgotPasswordResponse;
import com.bank.auth_service.api.response.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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

  @PostMapping("/logout")
  @ResponseStatus(HttpStatus.OK)
  @Operation(tags = {"Auths APIs"})
  @SecurityRequirement(name = "Bearer Authentication")
  @PreAuthorize("isAuthenticated()")
  public Mono<BaseResponse<Void>> logout() {
    return this.authFacade.logout();
  }

  @PostMapping("refresh-token")
  @ResponseStatus(HttpStatus.OK)
  @Operation(tags = {"Auths APIs"})
  public Mono<BaseResponse<LoginResponse>> refreshToken(@RequestBody RefreshTokenRequest request) {
    return this.authFacade.refresh(request);
  }

  @PostMapping("forgot-password")
  @ResponseStatus(HttpStatus.OK)
  @Operation(tags = {"Auths APIs"})
  public Mono<BaseResponse<ForgotPasswordResponse>> forgotPassword(
      @RequestBody ForgotPasswordRequest request) {
    return this.authFacade.forgotPassword(request);
  }

  @PutMapping("/reset-password")
  @ResponseStatus(HttpStatus.OK)
  @Operation(tags = {"Auths APIs"})
  @SecurityRequirement(name = "Authentication Bearer")
  @PreAuthorize("isAuthenticated()")
  public Mono<BaseResponse<Void>> restPassword(@RequestBody ResetPasswordRequest request) {
    return this.authFacade.resetPassword(request);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.OK)
  @Operation(tags = {"Auths APIs"})
  @SecurityRequirement(name = "Authentication Bearer")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public Mono<BaseResponse<Void>> createAccount(@RequestBody UpsertAccountRequest request) {
    return this.authFacade.createAccount(request);
  }
}
