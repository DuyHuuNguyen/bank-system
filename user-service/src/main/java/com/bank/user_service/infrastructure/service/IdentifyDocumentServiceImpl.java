package com.bank.user_service.infrastructure.service;

import com.bank.user_service.application.service.IdentifyDocumentService;
import com.bank.user_service.domain.entity.IdentifyDocument;
import com.bank.user_service.domain.repository.IdentifyDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class IdentifyDocumentServiceImpl implements IdentifyDocumentService {
  private final IdentifyDocumentRepository identifyDocumentRepository;

  @Override
  public Mono<IdentifyDocument> save(IdentifyDocument identifyDocument) {
    return this.identifyDocumentRepository.save(identifyDocument);
  }
}
