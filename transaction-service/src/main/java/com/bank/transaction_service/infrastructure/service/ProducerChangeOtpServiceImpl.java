package com.bank.transaction_service.infrastructure.service;

import com.bank.transaction_service.application.message.ChangeOtpMessage;
import com.bank.transaction_service.application.service.ProducerChangeOtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProducerChangeOtpServiceImpl implements ProducerChangeOtpService {
  private final RabbitTemplate rabbitTemplate;

  @Value("${james-config-rabbitmq.change-otp.exchange}")
  private String changeOtpExchange;

  @Value("${james-config-rabbitmq.change-otp.routing}")
  private String changeOtpRoutingKey;

  @Override
  public Mono<Void> sendChangeOtpMessage(ChangeOtpMessage changeOtpMessage) {
    return Mono.fromRunnable(
        () -> {
          this.rabbitTemplate.convertAndSend(
              this.changeOtpExchange, this.changeOtpRoutingKey, changeOtpMessage);
        });
  }
}
