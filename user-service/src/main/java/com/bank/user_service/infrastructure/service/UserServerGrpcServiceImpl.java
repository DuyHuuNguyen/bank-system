package com.bank.user_service.infrastructure.service;

import com.bank.user_service.application.exception.EntityNotFoundException;
import com.bank.user_service.application.service.PersonalInformationService;
import com.bank.user_service.infrastructure.enums.ErrorCode;
import com.example.server.user.UserNameRequest;
import com.example.server.user.UserNameResponse;
import com.example.server.user.UserServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import reactor.core.publisher.Mono;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class UserServerGrpcServiceImpl extends UserServiceGrpc.UserServiceImplBase {
  private final PersonalInformationService personalInformationService;

  @Override
  public void findNameByRequest(
      UserNameRequest request, StreamObserver<UserNameResponse> responseObserver) {
    this.personalInformationService
        .findByUserId(request.getId())
        .switchIfEmpty(Mono.error(new EntityNotFoundException(ErrorCode.PERSON_NOT_FOUND)))
        .map(
            personalInformation ->
                UserNameResponse.newBuilder()
                    .setFullName(
                        personalInformation
                            .getFirstName()
                            .concat(personalInformation.getLastName()))
                    .build())
        .doOnNext(ok -> log.info("resp {} ", ok))
        .onErrorResume(
            err -> {
              log.warn("Not found user ");
              return Mono.just(UserNameResponse.newBuilder().setFullName("").build());
            })
        .subscribe(
            response -> {
              responseObserver.onNext(response);
              responseObserver.onCompleted();
            });
  }
}
