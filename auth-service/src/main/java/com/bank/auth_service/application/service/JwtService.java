package com.bank.auth_service.application.service;

import java.util.concurrent.CompletableFuture;

public interface JwtService {
  CompletableFuture<String> generateAccessToken(String personalIdentificationNumber);

  CompletableFuture<String> generateRefreshToken(String personalIdentificationNumber);

  Boolean validateToken(String token);

  String getPersonalIdentificationNumberFromJwtToken(String token);

  String generateResetPasswordToken(String personalIdentificationNumber);
}
