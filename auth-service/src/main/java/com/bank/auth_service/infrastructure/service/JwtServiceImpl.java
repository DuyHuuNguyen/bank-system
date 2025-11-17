package com.bank.auth_service.infrastructure.service;

import com.bank.auth_service.application.exception.InvalidTokenException;
import com.bank.auth_service.application.service.JwtService;
import com.bank.auth_service.infrastructure.nums.ErrorCode;
import io.jsonwebtoken.*;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class JwtServiceImpl implements JwtService {
  @Value("${jwt.secretKey}")
  private String SECRET_KEY;

  @Value("${jwt.accessTokenExpirationTime}")
  private String ACCESS_TOKEN_EXPIRATION_TIME;

  @Value("${jwt.refreshTokenExpirationTime}")
  private String REFRESH_TOKEN_EXPIRATION_TIME;

  @Value("${jwt.resetPasswordTokenExpirationTime}")
  private String RESET_PASSWORD_TOKEN_EXPIRATION_TIME;

  @Override
  public CompletableFuture<String> generateAccessToken(String personalIdentificationNumber) {

    return CompletableFuture.supplyAsync(
        () ->
            Jwts.builder()
                .setSubject(personalIdentificationNumber)
                .setIssuedAt(new Date())
                .setExpiration(
                    new Date(
                        System.currentTimeMillis() + Long.parseLong(ACCESS_TOKEN_EXPIRATION_TIME)))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact());
  }

  @Override
  public CompletableFuture<String> generateRefreshToken(String personalIdentificationNumber) {

    return CompletableFuture.supplyAsync(
        () ->
            Jwts.builder()
                .setSubject(personalIdentificationNumber)
                .setIssuedAt(new Date())
                .setExpiration(
                    new Date(
                        System.currentTimeMillis() + Long.parseLong(REFRESH_TOKEN_EXPIRATION_TIME)))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact());
  }

  @Override
  public Mono<String> generateResetPasswordToken(String personalIdentificationNumber) {
    return Mono.fromFuture(
        CompletableFuture.supplyAsync(
            () ->
                Jwts.builder()
                    .setSubject(personalIdentificationNumber)
                    .setIssuedAt(new Date())
                    .setExpiration(
                        new Date(
                            System.currentTimeMillis()
                                + Long.parseLong(RESET_PASSWORD_TOKEN_EXPIRATION_TIME)))
                    .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                    .compact()));
  }

  @Override
  public Mono<String> getPersonalIdFromToken(String token) {
    return Mono.fromFuture(
        CompletableFuture.supplyAsync(
            () ->
                Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody()
                    .get("sub", String.class)));
  }

  @Override
  public Boolean validateToken(String token) {
    if (null == token) return false;
    try {
      Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parse(token);
    } catch (MalformedJwtException
        | ExpiredJwtException
        | UnsupportedJwtException
        | IllegalArgumentException exception) {
      throw new InvalidTokenException(ErrorCode.JWT_INVALID);
    }
    return true;
  }

  @Override
  public String getPersonalIdentificationNumberFromJwtToken(String token) {
    return Jwts.parser()
        .setSigningKey(SECRET_KEY)
        .parseClaimsJws(token)
        .getBody()
        .get("sub", String.class);
  }
}
