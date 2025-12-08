package com.bank.transaction_service.domain.repository;

import com.bank.transaction_service.domain.entity.Method;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface MethodRepository extends R2dbcRepository<Method, Long> {
  @Query("""
    SELECT m
    FROM methods m
  WHERE m.is_active and m.method_name =:methodName
  """)
  Mono<Method> findMethodByMethodName(String methodName);

  Mono<Method> findByMethodName(String methodName);

  @Query("""
    SELECT m
    FROM methods m
    LIMIT :pageSize OFFSET :offset
    """)
  Flux<Method> findAllByPageSizeAndOffset(Integer pageSize,Integer offset);
}
