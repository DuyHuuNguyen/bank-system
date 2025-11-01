package com.bank.auth_service.domain.entity;

import com.bank.auth_service.infrastructure.nums.RoleEnum;
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
@Table("roles")
public class Role extends BaseEntity {
  @Column("role_name")
  private RoleEnum roleName;
}
