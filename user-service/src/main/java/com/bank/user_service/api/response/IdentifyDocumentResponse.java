package com.bank.user_service.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class IdentifyDocumentResponse {
  private Long id;
  private String personalId;
  private Long issuedAt;
  private String citizenIdFront;
  private String citizenIdBack;
  private String country;
  private String province;
  private String district;
  private String ward;
  private String street;
  private String homesNumber;
}
