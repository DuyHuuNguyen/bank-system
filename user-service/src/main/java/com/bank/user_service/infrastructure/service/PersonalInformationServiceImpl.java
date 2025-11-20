package com.bank.user_service.infrastructure.service;

import com.bank.user_service.application.service.PersonalInformationService;
import com.bank.user_service.domain.entity.PersonalInformation;
import com.bank.user_service.domain.repository.PersonalInformationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PersonalInformationServiceImpl implements PersonalInformationService {
  private final PersonalInformationRepository personalInformationRepository;

  @Override
  public Mono<PersonalInformation> save(PersonalInformation personalInformation) {
    return this.personalInformationRepository.save(personalInformation);
  }
}
