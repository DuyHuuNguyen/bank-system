package com.bank.transaction_service.application.service;

import com.example.server.grpc.AuthResponse;
import reactor.core.publisher.Mono;

public interface AuthGrpcClientService {
  Mono<AuthResponse> parseToken(String token);
}
