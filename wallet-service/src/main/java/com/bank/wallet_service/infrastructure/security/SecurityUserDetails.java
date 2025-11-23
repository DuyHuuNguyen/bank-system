package com.bank.wallet_service.infrastructure.security;

import com.example.server.grpc.AuthResponse;
import java.util.Collection;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Builder
public class SecurityUserDetails implements UserDetails {
  @Getter private Long userId;
  @Getter private Long accountId;

  private String password;

  @Getter private String email;

  private String phone;

  @Getter private String otp;

  @Getter private String personalId;

  @Getter private Boolean isActive;

  @Getter private Collection<? extends GrantedAuthority> authorities;

  public static SecurityUserDetails build(AuthResponse authResponse) {
    return SecurityUserDetails.builder()
        .accountId(authResponse.getAccountId())
        .password("")
        .email(authResponse.getEmail())
        .phone(authResponse.getPhone())
        .otp(authResponse.getOtp())
        .personalId(authResponse.getPersonalId())
        .authorities(authResponse.getRolesList().stream().map(SimpleGrantedAuthority::new).toList())
        .isActive(authResponse.getIsActive())
        .build();
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public String getUsername() {
    return this.personalId;
  }
}
