package com.bank.transaction_service.infrastructure.util;

import com.bank.transaction_service.infrastructure.enums.TransactionMethodEnum;
import com.bank.transaction_service.infrastructure.enums.TransactionStatus;
import com.bank.transaction_service.infrastructure.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class TransactionCriteria {
   private Long sourceWalletId;
    private  Long beneficiaryWalletId;
    private  String methodName;
    private LocalDate transactionCreatedAt;
    private LocalDate transactionCreatedAroundMonthAt;
    private  TransactionType type;
    private  TransactionStatus status;
    private Integer pageSize;
    private Integer offset;
}
