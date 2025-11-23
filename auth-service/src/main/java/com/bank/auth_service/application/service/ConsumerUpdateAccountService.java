package com.bank.auth_service.application.service;

import com.bank.auth_service.application.message.UpdateAccountMessage;
import reactor.core.publisher.Mono;

public interface ConsumerUpdateAccountService {
  Mono<Void> onCreateAccountMessage(UpdateAccountMessage updateAccountMessage);
}
