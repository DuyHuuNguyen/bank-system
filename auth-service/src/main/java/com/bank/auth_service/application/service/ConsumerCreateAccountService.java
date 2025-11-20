package com.bank.auth_service.application.service;

import com.bank.auth_service.application.message.CreateAccountMessage;
import reactor.core.publisher.Mono;

public interface ConsumerCreateAccountService {
  Mono<Void> onCreateAccountMessage(CreateAccountMessage createAccountMessage);
}
