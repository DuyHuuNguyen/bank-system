package com.bank.user_service.domain.repository;

import com.bank.user_service.domain.entity.IdentifyDocument;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface IdentifyDocumentRepository extends R2dbcRepository<IdentifyDocument, Long> {

    @Query("""
    select *
    from identify_documents iden
    join users u 
    on iden.id = u.id
    where u.id =:userId
    """)
    Mono<IdentifyDocument> findByUserId(Long userId);
}
