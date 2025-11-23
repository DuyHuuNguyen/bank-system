package com.bank.user_service.application.service;

import com.bank.user_service.application.messsage.DefaultWalletMessage;
import reactor.core.publisher.Mono;

public interface ProducerCreateDefaultWalletService {
  Mono<Void> sendMessageCreateDefaultWallet(DefaultWalletMessage message);
}
