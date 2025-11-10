package com.bank.auth_service.domain.respository;

import com.bank.auth_service.domain.entity.Role;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface RoleRepository extends R2dbcRepository<Role, Long> {
    @Query(value = """
        SELECT  r.*
        FROM roles AS r
        INNER JOIN  account_roles AS ar
        ON r.id = ar.role_id
        WHERE ar.user_id =:accountId
    """)
    Flux<Role> findRolesByAccountId(Long accountId);
}
