package com.bank.user_service.infrastructure.rest.controller;

import com.bank.user_service.api.facade.IdentifyDocumentFacade;
import com.bank.user_service.api.request.ChangeIdentifyDocumentRequest;
import com.bank.user_service.api.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/identify-documents")
@RequiredArgsConstructor
public class IdentifyDocumentController {
  private final IdentifyDocumentFacade identifyDocumentFacade;

  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  @Operation(tags = "IDENTIFY DOCUMENT API")
  @PreAuthorize("isAuthenticated()")
  @SecurityRequirement(name = "Bearer Authentication")
  public Mono<BaseResponse<Void>> changeInfo(
      @PathVariable Long id, @RequestBody ChangeIdentifyDocumentRequest request) {
    request.withId(id);
    return this.identifyDocumentFacade.changeIdentifyDocument(request);
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  @Operation(tags = "IDENTIFY DOCUMENT API")
  @PreAuthorize("isAuthenticated()")
  @SecurityRequirement(name = "Bearer Authentication")
  public Mono<BaseResponse<Void>> findIdentifyDocumentById(@PathVariable Long id) {
    return this.identifyDocumentFacade.findIdentifyDocumentById(id);
  }
}
