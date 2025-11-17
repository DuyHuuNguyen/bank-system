package com.bank.auth_service.api.request;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UpsertAccountRequest {
  @Hidden private Long id;
  private String email;
  private String password;
  private String phone;
  private String personalId;
}
