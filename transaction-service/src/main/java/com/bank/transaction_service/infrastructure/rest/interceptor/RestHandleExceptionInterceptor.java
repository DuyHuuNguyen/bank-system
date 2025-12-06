package com.bank.transaction_service.infrastructure.rest.interceptor;

import com.bank.transaction_service.application.exception.EntityNotFoundException;
import com.bank.transaction_service.application.exception.IdempotencyException;
import com.bank.transaction_service.application.exception.OtpDontMatchingException;
import com.bank.transaction_service.application.exception.PersonalIdNotMatchException;
import java.net.URI;
import java.util.function.Consumer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestHandleExceptionInterceptor {
  @ExceptionHandler(EntityNotFoundException.class)
  public ProblemDetail handleException(EntityNotFoundException entityNotFoundException) {
    return build(
        HttpStatus.NOT_FOUND,
        entityNotFoundException,
        problem -> {
          problem.setType(URI.create("http://example.com/problems/"));
          problem.setTitle(entityNotFoundException.getMessage());
        });
  }

  @ExceptionHandler(OtpDontMatchingException.class)
  public ProblemDetail handleException(OtpDontMatchingException otpDontMatchingException) {
    return build(
        HttpStatus.NOT_FOUND,
        otpDontMatchingException,
        problem -> {
          problem.setType(URI.create("http://example.com/problems/"));
          problem.setTitle(otpDontMatchingException.getMessage());
        });
  }

  @ExceptionHandler(PersonalIdNotMatchException.class)
  public ProblemDetail handleException(PersonalIdNotMatchException personalIdNotMatchException) {
    return build(
        HttpStatus.NOT_FOUND,
        personalIdNotMatchException,
        problem -> {
          problem.setType(URI.create("http://example.com/problems/"));
          problem.setTitle(personalIdNotMatchException.getMessage());
        });
  }

  @ExceptionHandler(IdempotencyException.class)
  public ProblemDetail handleException(IdempotencyException exception) {
    return build(
        HttpStatus.BAD_REQUEST,
        exception,
        problem -> {
          problem.setType(URI.create("http://example.com/problems/"));
          problem.setTitle(exception.getMessage());
        });
  }

  private ProblemDetail build(HttpStatus status, Exception ex, Consumer<ProblemDetail> consumer) {
    var problem = ProblemDetail.forStatusAndDetail(status, ex.getMessage());
    consumer.accept(problem);
    return problem;
  }
}
