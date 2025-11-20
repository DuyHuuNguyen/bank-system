package com.bank.user_service.application.service;

import com.bank.user_service.domain.entity.IdentifyDocument;
import reactor.core.publisher.Mono;

public interface IdentifyDocumentService {
  Mono<IdentifyDocument> save(IdentifyDocument identifyDocument);
}
