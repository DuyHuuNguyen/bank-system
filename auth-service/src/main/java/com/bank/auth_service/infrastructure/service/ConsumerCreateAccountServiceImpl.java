package com.bank.auth_service.infrastructure.service;

import com.bank.auth_service.application.message.CreateAccountMessage;
import com.bank.auth_service.application.service.AccountRoleService;
import com.bank.auth_service.application.service.AccountService;
import com.bank.auth_service.application.service.ConsumerCreateAccountService;
import com.bank.auth_service.application.service.RoleService;
import com.bank.auth_service.domain.entity.Account;
import com.bank.auth_service.domain.entity.AccountRole;
import com.bank.auth_service.infrastructure.nums.RoleEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsumerCreateAccountServiceImpl implements ConsumerCreateAccountService {

  private final AccountService accountService;
  private final PasswordEncoder passwordEncoder;
  private final RoleService roleService;
  private final AccountRoleService accountRoleService;

  @Override
  @Transactional
  @RabbitListener(queues = "${rabbitmq.queue.create-account}")
  public Mono<Void> onCreateAccountMessage(CreateAccountMessage message) {
    log.info("Received create account message");

    return Mono.just(message)
        .flatMap(
            msg -> {
              String passwordEncoded = this.passwordEncoder.encode(msg.getPassword());
              Account newAccount =
                  Account.builder()
                      .email(msg.getEmail())
                      .phone(msg.getPhone())
                      .personalId(msg.getPersonalId())
                      .userId(msg.getUserId())
                      .password(passwordEncoded)
                      .build();

              return this.accountService
                  .save(newAccount)
                  .flatMap(
                      accountStored ->
                          this.roleService
                              .findByRoleName(RoleEnum.USER)
                              .switchIfEmpty(
                                  Mono.error(new RuntimeException("Role USER not found")))
                              .flatMap(
                                  role -> {
                                    AccountRole accountRole =
                                        AccountRole.builder()
                                            .accountId(accountStored.getId())
                                            .roleId(role.getId())
                                            .build();
                                    return this.accountRoleService
                                        .save(accountRole)
                                        .thenReturn(accountStored);
                                  }));
            })
        .doOnSuccess(accountStored -> log.info("Account created successfully"))
        .doOnError(err -> log.error("Failed to create account", err))
        .then();
  }
}
