package com.bank.user_service.api.request;

import com.bank.user_service.infrastructure.enums.Gender;
import com.bank.user_service.infrastructure.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CreateUserRequest {
  private String personalId;
  private Long issuedAt;
  private String citizenIdFront;
  private String citizenIdBack;
  private String firstName;
  private String lastName;
  private Long dateOfBirth;
  private Gender gender;
  private String personalPhoto;
  private String email;
  private String phone;
  private String password;
  private UserType userType;
}
