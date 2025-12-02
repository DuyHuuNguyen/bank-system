package com.bank.transaction_service.api.request;

import com.bank.transaction_service.infrastructure.enums.TransactionStatus;
import com.bank.transaction_service.infrastructure.enums.TransactionType;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TransactionCriteria extends BaseCriteria {
  @NotNull private Long sourceWalletId;
  private Long beneficiaryWalletId;
  private String methodName;
  private LocalDate transactionCreatedAt;
  private LocalDate transactionCreatedAroundMonthAt;
  private TransactionType type;
  private TransactionStatus status;
  @Hidden private Integer offset;

  public void addOffset() {
    this.offset = super.getCurrentPage() * super.getPageSize();
  }
}
