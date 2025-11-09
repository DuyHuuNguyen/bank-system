package com.bank.user_service.infrastructure.service;

import com.bank.user_service.application.service.PersonalInformationService;
import com.bank.user_service.domain.repository.PersonalInformationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonalInformationServiceImpl implements PersonalInformationService {
  private final PersonalInformationRepository personalInformationRepository;
}
