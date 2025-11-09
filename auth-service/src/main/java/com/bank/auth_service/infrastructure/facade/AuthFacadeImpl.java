package com.bank.auth_service.infrastructure.facade;

import com.bank.auth_service.api.facade.AuthFacade;
import com.bank.auth_service.api.request.LoginRequest;
import com.bank.auth_service.api.response.BaseResponse;
import com.bank.auth_service.api.response.LoginResponse;
import com.bank.auth_service.application.service.AccountService;
import com.bank.auth_service.application.service.CacheService;
import com.bank.auth_service.application.service.JwtService;
import com.bank.auth_service.infrastructure.nums.CacheTemplate;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuthFacadeImpl implements AuthFacade {
  private final AccountService accountService;
  private final JwtService jwtService;
  private final CacheService cacheService;
  private final ReactiveAuthenticationManager reactiveAuthenticationManager;

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

              CompletableFuture<Void> storeAccessTokenFuture =
                  CompletableFuture.runAsync(
                      () ->
                          this.cacheService.store(
                              accessTokenCacheKey, response.getAccessToken(), Duration.ofDays(3)));

              CompletableFuture<Void> storeRefreshTokenFuture =
                  CompletableFuture.runAsync(
                      () ->
                          this.cacheService.store(
                              refreshTokenCacheKey,
                              response.getRefreshToken(),
                              Duration.ofDays(14)));

              return Mono.fromFuture(
                      CompletableFuture.allOf(storeAccessTokenFuture, storeRefreshTokenFuture))
                  .thenReturn(BaseResponse.build(response, true));
            });
  }
}
