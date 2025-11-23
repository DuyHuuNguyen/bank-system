package com.bank.wallet_service.domain.entity;

import java.math.BigDecimal;
import lombok.*;
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
  @Builder.Default
  private BigDecimal availableBalance = BigDecimal.valueOf(0L);

  @Column("spending_balance")
  @Builder.Default
  private BigDecimal spendingBalance = BigDecimal.valueOf(0L);

  @Column("currency_id")
  private Long currencyId;

  @Column("user_id")
  private Long userId;

  @Column("is_default")
  private Boolean isDefault;
}
