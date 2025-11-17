package com.bank.auth_service.domain.respository;

import com.bank.auth_service.domain.entity.AccountRole;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface AccountRoleRepository extends R2dbcRepository<AccountRole, Long> {
    @Query("""
    Select *
    From account_roles ar
    Where ar.account_id =:accountId
    """)
    Flux<AccountRole> findByAccountId(Long accountId);

    Mono<Void> deleteByAccountId(Long accountId);
}
