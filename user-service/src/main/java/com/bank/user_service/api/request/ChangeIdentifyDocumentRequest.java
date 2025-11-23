package com.bank.user_service.api.request;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChangeIdentifyDocumentRequest {
  @Hidden private Long id;
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

  public void withId(Long id) {
    this.id = id;
  }
}
