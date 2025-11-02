package com.bank.user_service.domain.entity;

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
@Table("identify_documents")
public class IdentifyDocument extends BaseEntity {
  @Column("personal_id")
  private String personalId;

  @Column("issued_at")
  private Long issuedAt;

  @Column("citizen_id_front")
  private String citizenIdFront;

  @Column("citizen_id_back")
  private String citizenIdBack;

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
}
