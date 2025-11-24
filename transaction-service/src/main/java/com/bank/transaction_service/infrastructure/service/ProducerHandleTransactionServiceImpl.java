package com.bank.transaction_service.infrastructure.service;

import com.bank.transaction_service.api.request.CreateTransactionRequest;
import com.bank.transaction_service.application.message.TransactionMessage;
import com.bank.transaction_service.application.service.ProducerHandleTransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProducerHandleTransactionServiceImpl implements ProducerHandleTransactionService {
    private final RabbitTemplate rabbitTemplate;

    @Value("${james-config-rabbitmq.start-transaction.exchange.exchange}")
    private String exchangeName;

    @Value("${james-config-rabbitmq.start-transaction.routing.transfer-queue}")
    private String routingForTransfer;

    @Value("${james-config-rabbitmq.start-transaction.routing.deposit-queue}")
    private String routingForDeposit;

    @Value("${james-config-rabbitmq.start-transaction.routing.withdraw-queue}")
    private String routingForWithdraw;


    @Value("${james-config-rabbitmq.fail-transaction.exchange.exchange-fail}")
    private String failExchange;

    @Value("${james-config-rabbitmq.fail-transaction.routing.key-fail}")
    private String failRoutingKey;

    @Override
    public Mono<Void> sendMessageHandleTransaction(TransactionMessage message) {
       switch (message.getPaymentRouting()){
           case TRANSFER -> {
               log.info("transfer payment routing: {}", message.getPaymentRouting());
               return Mono.fromRunnable(()->this.rabbitTemplate.convertAndSend(exchangeName, routingForTransfer, message));
           }
           case DEPOSIT -> {
               log.info("deposit payment routing: {}", message.getPaymentRouting());
               return Mono.fromRunnable(()->this.rabbitTemplate.convertAndSend(exchangeName, routingForDeposit, message));
           }
           case WITHDRAW -> {
               log.info("withdraw payment routing: {}", message.getPaymentRouting());
               return Mono.fromRunnable(()->this.rabbitTemplate.convertAndSend(exchangeName, routingForWithdraw, message));
           }
           default -> {
               log.info("unknown payment routing: {}", message.getPaymentRouting());
           }
       }
        return Mono.fromRunnable(()->this.rabbitTemplate.convertAndSend(failExchange, failRoutingKey, message));
    }
}
