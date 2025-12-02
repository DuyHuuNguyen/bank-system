package com.bank.transaction_service.domain.repository;

import com.bank.transaction_service.application.dto.TransactionHistoryDTO;
import com.bank.transaction_service.domain.entity.Transaction;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface TransactionRepository extends R2dbcRepository<Transaction, Long> {
    @Query("""
    select t.id, t.transaction_balance 
     , t.source_wallet_id 
     ,t.beneficiary_wallet_id 
     , t.description 
     ,t.created_at 
     ,m.method_name
    from transactions t
    join transaction_methods tm
    on t.id = tm.transaction_id
    join methods m ON m.id = tm.method_id
    where 1=1 :conditions
    limit :pageSize offset :offsets
    """)
    Flux<TransactionHistoryDTO> findAll(String conditions,Integer pageSize,Integer offsets);
}
