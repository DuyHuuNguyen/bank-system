package com.bank.auth_service.api.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ChangeRoleAccountRequest {
  private Long accountId;
  private List<Long> roleIds;
}
