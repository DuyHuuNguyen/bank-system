package com.bank.auth_service.infrastructure.security;

import com.bank.auth_service.domain.entity.Account;
import java.util.Collection;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Builder
public class SecurityUserDetails implements UserDetails {

  @Getter private Long accountId;

  private String password;

  @Getter private String email;

  private String phone;

  @Getter private String otp;

  @Getter private String personalId;

  @Getter private Boolean isActive;

  @Getter private Collection<? extends GrantedAuthority> authorities;

  public static SecurityUserDetails build(
      Account account, Collection<? extends GrantedAuthority> authorities) {
    return SecurityUserDetails.builder()
        .accountId(account.getId())
        .password(account.getPassword())
        .email(account.getEmail())
        .phone(account.getPhone())
        .otp(account.getOtp())
        .personalId(account.getPersonalId())
        .authorities(authorities)
        .isActive(account.isActive())
        .build();
  }

  @Override
  public String getPassword() {
    return "";
  }

  @Override
  public String getUsername() {
    return this.personalId;
  }
}
