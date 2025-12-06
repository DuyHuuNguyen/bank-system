package com.bank.transaction_service.infrastructure.service;

import com.bank.transaction_service.application.service.AuthGrpcClientService;
import com.example.server.grpc.AccessTokenRequest;
import com.example.server.grpc.AuthResponse;
import com.example.server.grpc.AuthTokenServiceGrpc;
import java.util.Objects;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AuthGrpcClientServiceImpl implements AuthGrpcClientService {
  @GrpcClient("parse-token")
  private AuthTokenServiceGrpc.AuthTokenServiceBlockingStub authTokenServiceBlockingStub;

  @Override
  public Mono<AuthResponse> parseToken(String token) {
    AccessTokenRequest request = AccessTokenRequest.newBuilder().setAccessToken(token).build();
    return Mono.fromCallable(() -> authTokenServiceBlockingStub.parseToken(request))
        .materialize()
        .flatMap(
            signal -> {
              if (signal.isOnError()) {
                // Trả về 1 event khi có lỗi
                return Mono.just(AuthResponse.newBuilder().setIsEnabled(false).build());
              }
              // Trả về data gốc
              return Mono.just(Objects.requireNonNull(signal.get()));
            });
  }
}
