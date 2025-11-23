package com.bank.user_service.application.service;

import com.bank.user_service.application.messsage.CreateAccountMessage;
import reactor.core.publisher.Mono;

public interface ProducerCreateAccountService {
  Mono<Void> sendCreateAccountMessage(CreateAccountMessage createAccountMessage);
}
