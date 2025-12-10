package com.bank.transaction_service.domain.repository;

import com.bank.transaction_service.application.dto.TransactionHistoryDTO;
import com.bank.transaction_service.domain.entity.Transaction;
import com.bank.transaction_service.infrastructure.enums.TransactionStatus;
import com.bank.transaction_service.infrastructure.enums.TransactionType;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Repository
public interface TransactionRepository extends R2dbcRepository<Transaction, Long> {
    @Query("""
    select t.id, 
        t.transaction_balance 
     , t.source_wallet_id 
     ,t.beneficiary_wallet_id 
     , t.description 
     ,t.created_at 
     ,m.method_name
     ,t.transaction_type
    ,t.transaction_status
   ,t.currency
   ,t.reference_code
    from transactions t
    join transaction_methods tm
    on t.id = tm.transaction_id
    join methods m ON m.id = tm.method_id
    where t.id =:id
    """)
    Mono<TransactionHistoryDTO> findTransactionHistoryById(Long id);


    @Query("""
    SELECT  COALESCE(SUM(t.transaction_balance), 0)
    FROM transactions t
    WHERE 
        t.transaction_status =:status
        AND t.transaction_type =:type
        AND TO_TIMESTAMP(t.created_at / 1000.0) >= DATE_TRUNC('month', :month::date)
            AND TO_TIMESTAMP(t.created_at / 1000.0) <  DATE_TRUNC('month', :month::date) + INTERVAL '1 month'
        AND t.source_wallet_id = :walletId;
  """)
    Mono<Double> statisticTransactionBalance(TransactionStatus status, Long walletId,TransactionType type, String month);

}
