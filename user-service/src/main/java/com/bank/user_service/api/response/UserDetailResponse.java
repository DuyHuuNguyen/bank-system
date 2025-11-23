package com.bank.user_service.api.response;

import com.bank.user_service.application.dto.LocationDTO;
import com.bank.user_service.infrastructure.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class UserDetailResponse {
  private Long id;
  private Long identifyDocumentId;
  private String personalId;
  private Long issuesAt;
  private String citizenIdFront;
  private String citizenIdBack;
  private LocationDTO locationOfIdentifyDocument;
  private Long personalDocumentId;
  private String firstName;
  private String lastName;
  private Long dateOfBirth;
  private Gender gender;
  private LocationDTO locationOfUser;
}
