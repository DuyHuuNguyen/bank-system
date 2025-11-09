package com.bank.auth_service.infrastructure.service;

import com.bank.auth_service.application.exception.EntityNotFoundException;
import com.bank.auth_service.application.service.AccountService;
import com.bank.auth_service.application.service.JwtService;
import com.bank.auth_service.application.service.RoleService;
import com.bank.auth_service.domain.entity.Account;
import com.bank.auth_service.infrastructure.nums.ErrorCode;
import com.example.server.grpc.AccessTokenRequest;
import com.example.server.grpc.AuthResponse;
import com.example.server.grpc.AuthTokenServiceGrpc;
import io.grpc.stub.StreamObserver;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.devh.boot.grpc.server.service.GrpcService;
import reactor.core.publisher.Mono;

@Log4j2
@GrpcService
@RequiredArgsConstructor
public class GrpcServer extends AuthTokenServiceGrpc.AuthTokenServiceImplBase {
  private final JwtService jwtService;
  private final AccountService accountService;
  private final RoleService roleService;

  @Override
  public void parseToken(
      AccessTokenRequest request, StreamObserver<AuthResponse> responseObserver) {
    String accessToken = request.getAccessToken();
    var isValidateToken = this.jwtService.validateToken(accessToken);
    if (!isValidateToken)
      responseObserver.onError(new JwtException(ErrorCode.JWT_INVALID.getMessage()));

    String personalIdentifyInformation =
        this.jwtService.getPersonalIdentificationNumberFromJwtToken(accessToken);
    log.info("personalId : {}", personalIdentifyInformation);
    accountService
        .findByPersonalId(personalIdentifyInformation)
        .switchIfEmpty(Mono.error(new EntityNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND)))
        .flatMap(this::buildAuthResponse)
        .subscribe(
            response -> {
              log.info("{}", response.toString());
              responseObserver.onNext(response);
              responseObserver.onCompleted();
            },
            error -> {
              responseObserver.onError(error);
            });
  }

  private Mono<AuthResponse> buildAuthResponse(Account account) {
    return this.roleService
        .findRolesByAccountId(account.getId())
        .switchIfEmpty(Mono.error(new EntityNotFoundException(ErrorCode.ROLE_NOT_FOUND)))
        .map(role -> role.getRoleName().getName())
        .collectList()
        .flatMap(
            roles ->
                Mono.just(
                    AuthResponse.newBuilder()
                        .setAccountId(account.getId())
                        .setEmail(account.getEmail())
                        .setPhone(account.getPhone())
                        .setOtp(account.getOtp())
                        .setPersonalId(account.getPersonalId())
                        .setIsActive(account.isActive())
                        .setIsEnabled(true)
                        .addAllRoles(roles)
                        .build()));
  }
}
