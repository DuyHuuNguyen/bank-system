package com.bank.transaction_service.application.dto;

import com.bank.transaction_service.infrastructure.enums.TransactionMethodEnum;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Getter
public class TransactionHistoryDTO {
    private Long id;
    private String balance;
    private Long sourceWalletId;
    private Long destinationWalletId;
    private String description;
    private Long createdAt;
    private String methodName;
    private String status;
    private String type;
}
