package com.bank.auth_service.infrastructure.service;

import com.bank.auth_service.application.service.AccountRoleService;
import com.bank.auth_service.domain.respository.AccountRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountRoleServiceImpl implements AccountRoleService {
  private final AccountRoleRepository accountRoleRepository;
}
