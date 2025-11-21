package com.bank.user_service.infrastructure.service;

import com.bank.user_service.application.service.UserService;
import com.bank.user_service.domain.entity.User;
import com.bank.user_service.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;

  @Override
  public Mono<User> save(User user) {
    return this.userRepository.save(user);
  }

  @Override
  public Mono<User> findById(Long id) {
    return this.userRepository.findById(id);
  }
}
