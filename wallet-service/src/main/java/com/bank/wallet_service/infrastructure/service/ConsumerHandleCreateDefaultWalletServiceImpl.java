package com.bank.wallet_service.infrastructure.service;

import com.bank.wallet_service.application.exception.EntityNotFoundException;
import com.bank.wallet_service.application.message.DefaultWalletMessage;
import com.bank.wallet_service.application.service.ConsumerHandleCreateDefaultWalletService;
import com.bank.wallet_service.application.service.CurrencyService;
import com.bank.wallet_service.application.service.WalletService;
import com.bank.wallet_service.domain.entity.Wallet;
import com.bank.wallet_service.infrastructure.enums.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ConsumerHandleCreateDefaultWalletServiceImpl
    implements ConsumerHandleCreateDefaultWalletService {
  private final WalletService walletService;
  private final CurrencyService currencyService;
  private final String DEFAULT = "VND";

  @Override
  @Transactional
  @RabbitListener(queues = "${rabbit-MQ.queue-default-wallet}")
  public Mono<Void> onMessageDefaultWallet(DefaultWalletMessage message) {
    return Mono.just(message)
        .flatMap(
            defaultWalletMessage ->
                this.currencyService
                    .findByName(DEFAULT)
                    .switchIfEmpty(
                        Mono.error(new EntityNotFoundException(ErrorCode.CURRENCY_NOT_FOUND)))
                    .flatMap(
                        currency -> {
                          Wallet wallet =
                              Wallet.builder().isDefault(true).userId(message.getUserId()).build();
                          return this.walletService.save(wallet);
                        })
                    .then());
  }
}
