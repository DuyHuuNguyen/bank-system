package com.bank.wallet_service.infrastructure.service;

import com.bank.wallet_service.application.service.UserGrpcClientService;
import com.example.server.user.UserNameRequest;
import com.example.server.user.UserNameResponse;
import com.example.server.user.UserServiceGrpc;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class UserGrpcClientServiceImpl implements UserGrpcClientService {

  @GrpcClient("user-service")
  private UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub;

  @Override
  public Mono<UserNameResponse> findUserNameById(Long id) {
    UserNameRequest userNameRequest = UserNameRequest.newBuilder().setId(id).build();
    return Mono.fromCallable(() -> this.userServiceBlockingStub.findNameByRequest(userNameRequest))
        .materialize()
        .flatMap(
            signal -> {
              if (signal.isOnError()) {
                return Mono.just(UserNameResponse.newBuilder().build());
              }
              return Mono.just(Objects.requireNonNull(signal.get()));
            });
  }
}
