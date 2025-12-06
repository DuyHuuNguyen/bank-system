package com.bank.transaction_service.api.response;

import com.bank.transaction_service.application.dto.WalletDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class TransactionDetailResponse {
  private Long id;
  private String balance;
  private String currency;
  private String status;
  private String type;
  private String referenceCode;
  private WalletDTO sourceWallet;
  private WalletDTO beneficiaryWallet;
  private String description;
}
