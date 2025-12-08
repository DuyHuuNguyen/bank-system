package com.bank.transaction_service.application.message;

import com.bank.transaction_service.infrastructure.enums.PaymentRouting;
import com.bank.transaction_service.infrastructure.enums.TransactionMessageStatus;
import com.bank.transaction_service.infrastructure.enums.TransactionMethodEnum;
import java.math.BigDecimal;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
public class TransactionMessage {
  private Long transactionId;
  private PaymentRouting paymentRouting;
  private Long ownerTransactionId;
  private Long sourceWalletId;
  private Long destinationWalletId;
  private String description;
  private BigDecimal amount;
  private Long createdAt;
  private TransactionMessageStatus status;
  private TransactionMethodEnum transactionMethodEnum;

  public void setOwnerTransactionId(Long ownerTransactionId) {
    this.ownerTransactionId = ownerTransactionId;
  }

  public void changeStatus(TransactionMessageStatus status) {
    this.status = status;
  }

  public void addTransactionMethodEnum(TransactionMethodEnum transactionMethodEnum) {
    this.transactionMethodEnum = transactionMethodEnum;
  }

  public void addTransactionId(Long id) {
    this.transactionId = id;
  }
}
