package com.bank.user_service.infrastructure.service;

import com.bank.user_service.application.service.UserService;
import com.bank.user_service.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
}
