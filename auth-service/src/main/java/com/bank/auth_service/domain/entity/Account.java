package com.bank.auth_service.domain.entity;

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
@Table("accounts")
public class Account extends BaseEntity {
  @Column("email")
  private String email;

  @Column("password")
  private String password;

  @Column("phone")
  private String phone;

  @Column("personal_id")
  private String personalId;

  @Column("otp")
  private String otp;

  @Column("user_id")
  private Long userId;

  public void changePassword(String passwordEncoded) {
    this.password = passwordEncoded;
  }
}
