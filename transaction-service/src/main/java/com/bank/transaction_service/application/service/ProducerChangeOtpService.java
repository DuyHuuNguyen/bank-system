package com.bank.transaction_service.application.service;

import com.bank.transaction_service.application.message.ChangeOtpMessage;
import reactor.core.publisher.Mono;

public interface ProducerChangeOtpService {
  Mono<Void> sendChangeOtpMessage(ChangeOtpMessage changeOtpMessage);
}
