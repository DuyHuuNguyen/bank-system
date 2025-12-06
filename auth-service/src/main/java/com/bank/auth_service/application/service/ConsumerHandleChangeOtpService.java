package com.bank.auth_service.application.service;

import com.bank.auth_service.application.message.ChangeOtpMessage;
import reactor.core.publisher.Mono;

public interface ConsumerHandleChangeOtpService {
  Mono<Void> handleChangeOtp(ChangeOtpMessage changeOtpMessage);
}
