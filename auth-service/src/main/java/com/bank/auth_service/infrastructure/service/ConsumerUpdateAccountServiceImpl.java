package com.bank.auth_service.infrastructure.service;

import com.bank.auth_service.application.exception.EntityNotFoundException;
import com.bank.auth_service.application.message.UpdateAccountMessage;
import com.bank.auth_service.application.service.AccountService;
import com.bank.auth_service.application.service.ConsumerUpdateAccountService;
import com.bank.auth_service.infrastructure.nums.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsumerUpdateAccountServiceImpl implements ConsumerUpdateAccountService {
  private final AccountService accountService;

  @Override
  @Transactional
  @RabbitListener(queues = "${rabbitmq.queue.update-info-account}")
  public Mono<Void> onCreateAccountMessage(UpdateAccountMessage updateAccountMessage) {
    log.info("handle message update account");
    return Mono.just(updateAccountMessage)
        .flatMap(
            message ->
                this.accountService
                    .findById(message.getAccountId())
                    .switchIfEmpty(
                        Mono.error(new EntityNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND)))
                    .flatMap(
                        account -> {
                          account.changePersonalId(message.getPersonalId());
                          return this.accountService.save(account);
                        })
                    .then());
  }
}
