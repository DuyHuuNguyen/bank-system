package com.bank.user_service.domain.entity;

import com.bank.user_service.api.request.ChangePersonalInfoRequest;
import com.bank.user_service.infrastructure.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table("personal_information")
public class PersonalInformation extends BaseEntity {
  @Column("first_name")
  private String firstName;

  @Column("last_name")
  private String lastName;

  @Column("date_of_birth")
  private Long dateOfBirth;

  @Column("gender")
  private Gender gender;

  @Column("personal_photo")
  private String personalPhoto;

  @Column("country")
  private String country;

  @Column("province")
  private String province;

  @Column("district")
  private String district;

  @Column("ward")
  private String ward;

  @Column("street")
  private String street;

  @Column("home_number")
  private String homesNumber;

  public void changeInfo(ChangePersonalInfoRequest request) {
    this.firstName = request.getFirstName();
    this.lastName = request.getLastName();
    this.dateOfBirth = request.getDateOfBirth();
    this.gender = request.getGender();
    this.personalPhoto = request.getPersonalPhoto();
    this.country = request.getCountry();
    this.province = request.getProvince();
    this.district = request.getDistrict();
    this.ward = request.getWard();
    this.street = request.getStreet();
    this.homesNumber = request.getHomesNumber();
  }

}
