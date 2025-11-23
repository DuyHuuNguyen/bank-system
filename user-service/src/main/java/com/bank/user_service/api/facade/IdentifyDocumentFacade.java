package com.bank.user_service.api.facade;

import com.bank.user_service.api.request.ChangeIdentifyDocumentRequest;
import com.bank.user_service.api.response.BaseResponse;
import reactor.core.publisher.Mono;

public interface IdentifyDocumentFacade {
  Mono<BaseResponse<Void>> changeIdentifyDocument(ChangeIdentifyDocumentRequest request);
}
