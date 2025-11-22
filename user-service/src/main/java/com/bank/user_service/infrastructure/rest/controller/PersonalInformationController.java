package com.bank.user_service.infrastructure.rest.controller;

import com.bank.user_service.api.facade.PersonalInformationFacade;
import com.bank.user_service.api.request.ChangePersonalInfoRequest;
import com.bank.user_service.api.response.BaseResponse;
import com.bank.user_service.api.response.PersonalInformationResponse;
import com.bank.user_service.application.service.PersonalInformationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/personal-information")
@RequiredArgsConstructor
public class PersonalInformationController {
    private final PersonalInformationFacade personalInformationFacade;

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(tags = "PERSONAL INFO API")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "Bearer Authentication")
    public Mono<BaseResponse<Void>> changePersonalInfo(@RequestBody ChangePersonalInfoRequest request){
        return this.personalInformationFacade.changePersonalInfo(request);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(tags = "PERSONAL INFO API")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "Bearer Authentication")
    public Mono<BaseResponse<PersonalInformationResponse>> findMyInfo(){
        return this.personalInformationFacade.findMyInfo();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(tags = "PERSONAL INFO API")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public Mono<BaseResponse<PersonalInformationResponse>> findPersonalInformationById(@PathVariable Long id){
        return this.personalInformationFacade.findPersonalInformationById(id);
    }

    
}
