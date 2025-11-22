package com.bank.user_service.api.response;

import com.bank.user_service.infrastructure.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class PersonalInformationResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private Long dateOfBirth;
    private Gender gender;
    private String personalPhoto;
    private String country;
    private String province;
    private String district;
    private String ward;
    private String street;
    private String homesNumber;
}
