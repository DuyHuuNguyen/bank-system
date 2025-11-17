package com.bank.auth_service.infrastructure.facade;

import com.bank.auth_service.api.facade.AuthFacade;
import com.bank.auth_service.api.request.*;
import com.bank.auth_service.api.response.BaseResponse;
import com.bank.auth_service.api.response.ForgotPasswordResponse;
import com.bank.auth_service.api.response.LoginResponse;
import com.bank.auth_service.application.exception.CacheException;
import com.bank.auth_service.application.exception.EntityNotFoundException;
import com.bank.auth_service.application.exception.PermissionDeniedException;
import com.bank.auth_service.application.service.AccountService;
import com.bank.auth_service.application.service.CacheService;
import com.bank.auth_service.application.service.JwtService;
import com.bank.auth_service.domain.entity.Account;
import com.bank.auth_service.infrastructure.nums.CacheTemplate;
import com.bank.auth_service.infrastructure.nums.ErrorCode;
import com.bank.auth_service.infrastructure.security.SecurityUserDetails;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Log4j2
@Service
@RequiredArgsConstructor
public class AuthFacadeImpl implements AuthFacade {
  private final AccountService accountService;
  private final JwtService jwtService;
  private final CacheService cacheService;
  private final ReactiveAuthenticationManager reactiveAuthenticationManager;
  private final PasswordEncoder passwordEncoder;

  @Override
  public Mono<BaseResponse<LoginResponse>> login(LoginRequest loginRequest) {
    return this.reactiveAuthenticationManager
        .authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getPersonalId(), loginRequest.getPassword()))
        .flatMap(
            authentication -> {
              CompletableFuture<String> accessTokenFuture =
                  this.jwtService.generateAccessToken(loginRequest.getPersonalId());
              CompletableFuture<String> refreshTokenFuture =
                  this.jwtService.generateRefreshToken(loginRequest.getPersonalId());

              CompletableFuture<LoginResponse> completableFuture =
                  CompletableFuture.allOf(accessTokenFuture, refreshTokenFuture)
                      .thenCompose(
                          v ->
                              accessTokenFuture.thenCombine(
                                  refreshTokenFuture,
                                  (accessToken, refreshToken) ->
                                      LoginResponse.builder()
                                          .accessToken(accessToken)
                                          .refreshToken(refreshToken)
                                          .build()));
              return Mono.fromFuture(completableFuture);
            })
        .flatMap(
            response -> {
              var accessTokenCacheKey =
                  String.format(
                      CacheTemplate.ACCESS_TOKEN_KEY.getContent(), loginRequest.getPersonalId());
              var refreshTokenCacheKey =
                  String.format(
                      CacheTemplate.REFRESH_TOKEN_KEY.getContent(), loginRequest.getPersonalId());

              Mono<Boolean> storeAccessToken =
                  this.cacheService.store(
                      accessTokenCacheKey, response.getAccessToken(), Duration.ofDays(3));
              Mono<Boolean> storeRefreshToken =
                  this.cacheService.store(
                      refreshTokenCacheKey, response.getRefreshToken(), Duration.ofDays(14));

              return Mono.zip(storeAccessToken, storeRefreshToken)
                  .doOnError(
                      cacheIsError -> {
                        throw new CacheException(ErrorCode.STORE_IS_ERROR);
                      })
                  .thenReturn(BaseResponse.build(response, true));
            });
  }

  @Override
  public Mono<BaseResponse<Void>> logout() {
    return ReactiveSecurityContextHolder.getContext()
        .map(SecurityContext::getAuthentication)
        .map(authentication -> (SecurityUserDetails) authentication.getPrincipal())
        .flatMap(
            principal -> {
              var personalId = principal.getPersonalId();
              var refreshTokenCacheKey =
                  String.format(CacheTemplate.REFRESH_TOKEN_KEY.getContent(), personalId);
              var accessTokenKey =
                  String.format(CacheTemplate.ACCESS_TOKEN_KEY.getContent(), personalId);

              Mono<Boolean> removeAccessToken = this.cacheService.remove(accessTokenKey);
              Mono<Boolean> removeRefreshToken = this.cacheService.remove(refreshTokenCacheKey);

              return Mono.zip(removeAccessToken, removeRefreshToken)
                  .doOnError(
                      cacheIsError -> {
                        throw new CacheException(ErrorCode.STORE_IS_ERROR);
                      })
                  .thenReturn(BaseResponse.ok());
            });
  }

  @Override
  public Mono<BaseResponse<LoginResponse>> refresh(RefreshTokenRequest request) {

    return this.jwtService
        .getPersonalIdFromToken(request.getRefreshToken())
        .switchIfEmpty(Mono.error(new EntityNotFoundException(ErrorCode.REFRESH_TOKEN_NOT_FOUND)))
        .doOnError(
            cacheIsError -> {
              throw new EntityNotFoundException(ErrorCode.REFRESH_TOKEN_NOT_FOUND);
            })
        .flatMap(
            personalId -> {
              var refreshTokenCacheKey =
                  String.format(CacheTemplate.REFRESH_TOKEN_KEY.getContent(), personalId);
              return this.cacheService
                  .hasKey(refreshTokenCacheKey)
                  .doOnError(
                      cacheIsError -> {
                        throw new CacheException(ErrorCode.STORE_IS_ERROR);
                      })
                  .flatMap(
                      isExited -> {
                        if (!isExited) return Mono.just(BaseResponse.fail());

                        var accessTokenCacheKey =
                            String.format(CacheTemplate.ACCESS_TOKEN_KEY.getContent(), personalId);
                        return Mono.fromFuture(this.jwtService.generateAccessToken(personalId))
                            .doOnError(
                                cacheIsError -> {
                                  throw new CacheException(ErrorCode.STORE_IS_ERROR);
                                })
                            .flatMap(
                                accessToken ->
                                    this.cacheService
                                        .store(accessTokenCacheKey, accessToken, Duration.ofDays(3))
                                        .thenReturn(
                                            BaseResponse.build(
                                                LoginResponse.builder()
                                                    .accessToken(accessToken)
                                                    .refreshToken(request.getRefreshToken())
                                                    .build(),
                                                true)));
                      });
            });
  }

  @Override
  public Mono<BaseResponse<ForgotPasswordResponse>> forgotPassword(ForgotPasswordRequest request) {
    return this.accountService
        .findByPersonalId(request.getPersonalId())
        .switchIfEmpty(Mono.error(new EntityNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND)))
        .flatMap(
            account ->
                this.jwtService
                    .generateResetPasswordToken(account.getPersonalId())
                    .doOnError(
                        cacheIsError -> {
                          throw new EntityNotFoundException(ErrorCode.JWT_INVALID);
                        })
                    .map(
                        resetPasswordToken ->
                            BaseResponse.build(
                                ForgotPasswordResponse.builder()
                                    .resetPasswordToken(resetPasswordToken)
                                    .build(),
                                true)));
  }

  @Override
  @Transactional
  public Mono<BaseResponse<Void>> resetPassword(ResetPasswordRequest request) {
    return ReactiveSecurityContextHolder.getContext()
        .map(SecurityContext::getAuthentication)
        .map(authentication -> (SecurityUserDetails) authentication.getPrincipal())
        .flatMap(
            principal -> {
              boolean validPassword = request.getPassword().equals(request.getPasswordConfig());
              if (!validPassword)
                return Mono.error(new PermissionDeniedException(ErrorCode.PASSWORD_DONT_MATCH));
              return this.accountService
                  .findByPersonalId(principal.getPersonalId())
                  .flatMap(
                      account -> {
                        String passwordEncoded = this.passwordEncoder.encode(request.getPassword());
                        account.changePassword(passwordEncoded);
                        return this.accountService
                            .save(account)
                            .map(accountStored -> BaseResponse.ok());
                      });
            });
  }

  @Override
  @Transactional
  public Mono<BaseResponse<Void>> createAccount(UpsertAccountRequest request) {
    String passwordEncoded = this.passwordEncoder.encode(request.getPassword());
    Account newAccount =
        Account.builder()
            .email(request.getEmail())
            .phone(request.getPhone())
            .personalId(request.getPersonalId())
            .password(passwordEncoded)
            .isActive(false)
            .build();
    return this.accountService
        .save(newAccount)
        .switchIfEmpty(Mono.error(new EntityNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND)))
        .map(accountStored -> BaseResponse.ok());
  }
}
