package com.bank.user_service.api.request;

import com.bank.user_service.infrastructure.enums.Gender;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ChangePersonalInfoRequest {
    @Hidden
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
