package com.bank.auth_service.infrastructure.nums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CacheTemplate {
  ACCESS_TOKEN_KEY("access_token_%s"),
  REFRESH_TOKEN_KEY("refresh_token_%s");
  private final String content;
}
