package com.bank.auth_service.infrastructure.service;

import com.bank.auth_service.application.service.RoleService;
import com.bank.auth_service.domain.respository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
}
