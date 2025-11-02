package com.bank.transaction_service.domain.entity;

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
@Table("fees")
public class Fee extends BaseEntity {
  @Column("name_fee")
  private String name;

  @Column("balance")
  private BigDecimal balance;

  @Column("currency")
  private String currency;
}
