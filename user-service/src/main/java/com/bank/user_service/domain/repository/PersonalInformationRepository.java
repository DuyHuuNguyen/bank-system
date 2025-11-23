package com.bank.user_service.domain.repository;

import com.bank.user_service.domain.entity.PersonalInformation;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface PersonalInformationRepository extends R2dbcRepository<PersonalInformation, Long> {
    @Query("""
    SELECT pi.*
    FROM users u
    JOIN personal_information pi
    ON u.personal_information_id = pi.id
    WHERE u.id =:id
    """)
    Mono<PersonalInformation> findByUserId(Long id);
}
