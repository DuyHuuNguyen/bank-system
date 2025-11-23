package com.bank.user_service.api.facade;

import com.bank.user_service.api.request.ChangeIdentifyDocumentRequest;
import com.bank.user_service.api.response.BaseResponse;
import com.bank.user_service.api.response.IdentifyDocumentResponse;
import reactor.core.publisher.Mono;

public interface IdentifyDocumentFacade {
  Mono<BaseResponse<Void>> changeIdentifyDocument(ChangeIdentifyDocumentRequest request);

  Mono<BaseResponse<IdentifyDocumentResponse>> findIdentifyDocumentById(Long id);
}
