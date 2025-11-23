package com.bank.wallet_service.application.service;

import com.bank.wallet_service.application.message.DefaultWalletMessage;
import reactor.core.publisher.Mono;

public interface ConsumerHandleCreateDefaultWalletService {
  Mono<Void> onMessageDefaultWallet(DefaultWalletMessage message);
}
