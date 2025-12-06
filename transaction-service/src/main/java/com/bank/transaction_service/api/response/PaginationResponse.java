package com.bank.transaction_service.api.response;

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
  private List<T> data;
  private Integer currentPage;
  private Integer pageSize;
}
