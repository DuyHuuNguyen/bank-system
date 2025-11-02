package com.bank.auth_service.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table("account_roles")
public class AccountRole extends BaseEntity {
  @Column("user_id")
  private Long accountId;

  @Column("role_id")
  private Long roleId;
}
