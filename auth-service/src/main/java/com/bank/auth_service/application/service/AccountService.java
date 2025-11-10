package com.bank.auth_service.application.service;

import com.bank.auth_service.domain.entity.Account;
import reactor.core.publisher.Mono;

public interface AccountService {
  Mono<Account> findByPersonalId(String personalId);
}
