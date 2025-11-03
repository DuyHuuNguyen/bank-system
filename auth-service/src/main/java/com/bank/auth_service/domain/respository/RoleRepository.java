package com.bank.auth_service.domain.respository;

import com.bank.auth_service.domain.entity.Role;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends R2dbcRepository<Role, Long> {
}
