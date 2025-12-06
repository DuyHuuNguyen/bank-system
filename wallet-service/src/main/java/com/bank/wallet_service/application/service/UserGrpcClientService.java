package com.bank.wallet_service.application.service;

import com.example.server.user.UserNameResponse;
import reactor.core.publisher.Mono;

public interface UserGrpcClientService {
  Mono<UserNameResponse> findUserNameById(Long id);
}
