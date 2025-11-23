package com.bank.wallet_service.domain.entity;

import com.bank.wallet_service.api.request.UpsertCurrencyRequest;
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
@Table("currencies")
public class Currency extends BaseEntity {
  @Column("currency_name")
  private String currencyName;

  public void changeName(String currencyName) {
    this.currencyName = currencyName;
  }

  public void changeInfo(UpsertCurrencyRequest request) {
    this.currencyName = request.getCurrencyName();
    if (request.getIsActive()) this.enable();
    else this.disable();
  }
}
