package com.bank.transaction_service.domain.entity;

import com.bank.transaction_service.infrastructure.enums.TransactionStatus;
import com.bank.transaction_service.infrastructure.enums.TransactionType;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table("transactions")
public class Transaction extends BaseEntity {

  @Column("transaction_balance")
  private BigDecimal transactionBalance;

  @Column("currency")
  private String currency;

  @Column("transaction_status")
  private TransactionStatus status;

  @Column("transaction_type")
  private TransactionType type;

  @Column("reference_code")
  private String referenceCode;

  @Column("source_wallet_id")
  private Long sourceWalletId;

  @Column("beneficiary_wallet_id")
  private Long beneficiaryWalletId;

  @Column("description")
  private String description;
}
