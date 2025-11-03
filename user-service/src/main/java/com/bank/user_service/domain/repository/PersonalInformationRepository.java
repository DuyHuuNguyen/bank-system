package com.bank.user_service.domain.repository;

import com.bank.user_service.domain.entity.PersonalInformation;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalInformationRepository extends R2dbcRepository<PersonalInformation, Long> {
}
