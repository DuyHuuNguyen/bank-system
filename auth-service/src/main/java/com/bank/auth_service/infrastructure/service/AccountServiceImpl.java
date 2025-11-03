package com.bank.auth_service.infrastructure.service;

import com.bank.auth_service.application.service.AccountService;
import com.bank.auth_service.domain.respository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
}
