package com.bank.transaction_service.infrastructure.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SecurityReactiveUserDetailsService implements ReactiveUserDetailsService {

  @Override
  public Mono<UserDetails> findByUsername(String username) {
    return null;
  }
}
