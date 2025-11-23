package com.bank.wallet_service.api.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class PaginationResponse<T> {
  private Integer pageSize;
  private Integer currentPage;
  private List<T> data;
}
