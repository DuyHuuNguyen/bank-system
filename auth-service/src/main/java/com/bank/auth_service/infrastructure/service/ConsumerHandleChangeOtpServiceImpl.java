package com.bank.auth_service.infrastructure.service;

import com.bank.auth_service.application.exception.EntityNotFoundException;
import com.bank.auth_service.application.message.ChangeOtpMessage;
import com.bank.auth_service.application.service.AccountService;
import com.bank.auth_service.application.service.ConsumerHandleChangeOtpService;
import com.bank.auth_service.infrastructure.nums.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsumerHandleChangeOtpServiceImpl implements ConsumerHandleChangeOtpService {
  private final AccountService accountService;

  @Override
  @RabbitListener(queues = "${rabbitmq.queue.change-otp}")
  public Mono<Void> handleChangeOtp(ChangeOtpMessage changeOtpMessage) {
    return this.accountService
        .findByPersonalId(changeOtpMessage.getPersonalId())
        .flatMap(
            account -> {
              if (account == null) {
                log.warn("Change otp: received a message and  can't find account");
                return Mono.error(new EntityNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));
              }
              account.changeOtp(changeOtpMessage.getNewOtp());
              return this.accountService
                  .save(account)
                  .doOnSuccess(ok -> log.info("Change otp ok!"))
                  .then();
            });
  }
}
