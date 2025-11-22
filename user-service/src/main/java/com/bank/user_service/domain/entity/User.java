package com.bank.user_service.domain.entity;

import com.bank.user_service.infrastructure.enums.UserType;
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
@Table("users")
public class User extends BaseEntity {
  @Column("user_type")
  private UserType userType;

  @Column("personal_information_id")
  private Long personalInformationId;

  @Column("identify_document_id")
  private Long identifyDocumentId;

  public void changeUserType(UserType userType) {
    this.userType = userType;
  }
}
