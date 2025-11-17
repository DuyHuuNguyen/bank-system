package com.bank.auth_service.infrastructure.service;

import com.bank.auth_service.application.service.AccountService;
import com.bank.auth_service.domain.entity.Account;
import com.bank.auth_service.domain.respository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
  private final AccountRepository accountRepository;

  @Override
  public Mono<Account> findByPersonalId(String personalId) {
    return this.accountRepository.findByPersonalId(personalId);
  }

  @Override
  public Mono<Account> save(Account account) {
    if (!account.isFirstSave()) account.reUpdate();
    return this.accountRepository.save(account);
  }
}
