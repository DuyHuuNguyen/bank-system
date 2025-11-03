package com.bank.user_service.domain.repository;

import com.bank.user_service.domain.entity.IdentifyDocument;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IdentifyDocumentRepository extends R2dbcRepository<IdentifyDocument, Long> {
}
