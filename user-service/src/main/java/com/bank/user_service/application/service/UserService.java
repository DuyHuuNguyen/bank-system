package com.bank.user_service.application.service;

import com.bank.user_service.domain.entity.User;
import reactor.core.publisher.Mono;

public interface UserService {
  Mono<User> save(User user);
}
