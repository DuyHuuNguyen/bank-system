package com.bank.auth_service.infrastructure.config;

import com.bank.auth_service.application.service.AccountService;
import com.bank.auth_service.application.service.JwtService;
import com.bank.auth_service.application.service.RoleService;
import com.bank.auth_service.infrastructure.rest.interceptor.AuthTokenInterceptor;
import com.bank.auth_service.infrastructure.security.SecurityReactiveUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
  private final SecurityReactiveUserDetailsService securityReactiveUserDetailsService;
  private final JwtService jwtService;
  private final AccountService accountService;
  private final RoleService roleService;

  private static final String[] WHITE_LISTS = {
    "/swagger-ui.html",
    "/swagger-ui/**",
    "/v3/api-docs/**",
    "/actuator/**",
    "/actuator/**",
    "/api/v1/auths/login",
    "/api/v1/auths/refresh-token",
    "/api/v1/auths/forgot-password"
  };

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public ReactiveAuthenticationManager authenticationManager(PasswordEncoder passwordEncoder) {
    UserDetailsRepositoryReactiveAuthenticationManager reactiveAuthenticationManager =
        new UserDetailsRepositoryReactiveAuthenticationManager(securityReactiveUserDetailsService);
    reactiveAuthenticationManager.setPasswordEncoder(passwordEncoder);
    return reactiveAuthenticationManager;
  }

  @Bean
  public SecurityWebFilterChain springSecurityFilterChain(
      ServerHttpSecurity http, ReactiveAuthenticationManager reactiveAuthenticationManager) {
    http.csrf(ServerHttpSecurity.CsrfSpec::disable)
        .cors(ServerHttpSecurity.CorsSpec::disable)
        .httpBasic(
            httpBasicSpec ->
                httpBasicSpec.authenticationEntryPoint(
                    new HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED)))
        .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
        .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
        .authorizeExchange(
            exchanges ->
                exchanges.pathMatchers(WHITE_LISTS).permitAll().anyExchange().authenticated())
        .addFilterBefore(
            new AuthTokenInterceptor(this.jwtService, this.accountService, this.roleService),
            SecurityWebFiltersOrder.HTTP_BASIC)
        .authenticationManager(reactiveAuthenticationManager);
    return http.build();
  }
}
