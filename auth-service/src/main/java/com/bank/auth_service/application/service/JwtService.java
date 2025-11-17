package com.bank.auth_service.application.service;

import java.util.concurrent.CompletableFuture;
import reactor.core.publisher.Mono;

public interface JwtService {
  CompletableFuture<String> generateAccessToken(String personalIdentificationNumber);

  CompletableFuture<String> generateRefreshToken(String personalIdentificationNumber);

  Boolean validateToken(String token);

  String getPersonalIdentificationNumberFromJwtToken(String token);

  Mono<String> generateResetPasswordToken(String personalIdentificationNumber);

  Mono<String> getPersonalIdFromToken(String token);
}
