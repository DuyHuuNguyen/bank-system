package com.bank.wallet_service.domain.entity;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table("wallets")
public class Wallet extends BaseEntity {
  @Column("available_balance")
  private BigDecimal availableBalance;

  @Column("spending_balance")
  private BigDecimal spendingBalance;

  @Column("currency_id")
  private Long currencyId;

  @Column("user_id")
  private Long userId;
}
