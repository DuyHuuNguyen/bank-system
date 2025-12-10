package com.bank.transaction_service.api.request;

import com.bank.transaction_service.infrastructure.enums.TransactionStatus;
import com.bank.transaction_service.infrastructure.enums.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@AllArgsConstructor
@NoArgsConstructor
@Data
@NotNull
public class StatisticTransactionBalanceRequest {
  private TransactionType transactionType;
  private TransactionStatus transactionStatus;
  private String year;

  @Schema(description = "The month must have pattern : '09' ")
  private String month;

  private Long walletId;

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
  }
}
