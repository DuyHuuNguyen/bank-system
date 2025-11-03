package com.bank.auth_service.domain.respository;

import com.bank.auth_service.domain.entity.AccountRole;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRoleRepository extends R2dbcRepository<AccountRole, Long> {
}
