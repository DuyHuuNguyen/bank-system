package com.bank.user_service.infrastructure.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
  USER_NOT_FOUND("User not found"),
  IDENTITY_DOCUMENT_NOT_FOUND("Identity document not found"),
  PERSON_NOT_FOUND("Person info not found"),
  STORE_INFO_USER_ERROR("Can't store user info"),
  PERSONAL_INFORMATION_NOT_FOUND("Personal information not found"),
  IDENTIFY_DOCUMENT_NOT_FOUND("Identify document not found");
  private final String message;
}
