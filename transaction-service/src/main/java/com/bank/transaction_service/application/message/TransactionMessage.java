package com.bank.transaction_service.application.message;

import com.bank.transaction_service.infrastructure.enums.PaymentRouting;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class TransactionMessage {
    private PaymentRouting paymentRouting;
    private Long fromWalletId;
    private Long toWalletId;
    private String description;
    private BigDecimal amount;

}
