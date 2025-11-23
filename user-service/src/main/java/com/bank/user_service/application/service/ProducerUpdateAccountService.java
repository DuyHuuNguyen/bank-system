package com.bank.user_service.application.service;

import com.bank.user_service.application.messsage.UpdateAccountMessage;
import reactor.core.publisher.Mono;

public interface ProducerUpdateAccountService {
  Mono<Void> sendMessageUpdateAccount(UpdateAccountMessage updateAccountMessage);
}
