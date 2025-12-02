package com.bank.transaction_service.infrastructure.service;

import com.bank.transaction_service.application.dto.TransactionHistoryDTO;
import com.bank.transaction_service.application.service.TransactionService;
import com.bank.transaction_service.domain.entity.Transaction;
import com.bank.transaction_service.domain.repository.TransactionRepository;
import com.bank.transaction_service.infrastructure.enums.TransactionMethodEnum;
import com.bank.transaction_service.infrastructure.enums.TransactionStatus;
import com.bank.transaction_service.infrastructure.util.TransactionCriteria;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
  private final TransactionRepository transactionRepository;
  private final R2dbcEntityTemplate r2dbcEntityTemplate;
  private final DatabaseClient databaseClient;

  @Override
  public Mono<Transaction> save(Transaction transaction) {
    if (transaction.getId() != null) transaction.reUpdate();
    return this.transactionRepository.save(transaction);
  }

  @Override
  public Flux<TransactionHistoryDTO> findAll(String conditions, Integer pageSize, Integer pageNumber) {
    int offset = (pageNumber - 1) * pageSize;
    return this.transactionRepository.findAll(conditions,pageSize,offset);
  }

  @Override
  public Flux<TransactionHistoryDTO> findAll(TransactionCriteria criteria) {
    StringBuilder sql = new StringBuilder("""
        select t.id, t.transaction_balance
                ,t.source_wallet_id
                ,t.beneficiary_wallet_id
               ,t.description
               ,t.created_at
               ,m.method_name
               ,t.transaction_type
               ,t.transaction_status
        from transactions t
        join transaction_methods tm on t.id = tm.transaction_id
        join methods m on m.id = tm.method_id
        where 1=1
    """);
    Map<String, Object> params = new HashMap<>();

    if (criteria.getSourceWalletId() != null) {
      sql.append(" AND t.source_wallet_id = :sourceWalletId");
      params.put("sourceWalletId", criteria.getSourceWalletId());
    }

    if (criteria.getBeneficiaryWalletId() != null) {
      sql.append(" AND t.beneficiary_wallet_id = :beneficiaryWalletId");
      params.put("beneficiaryWalletId", criteria.getBeneficiaryWalletId());
    }

    if (criteria.getMethodName() != null) {
      sql.append(" AND m.method_name = :methodName");
      params.put("methodName", criteria.getMethodName());
    }

    if (criteria.getType() != null) {
      sql.append(" AND t.transaction_type = :type");
      params.put("type", criteria.getType().toString());
    }

    if (criteria.getStatus() != null) {
      sql.append(" AND t.transaction_status = :status");
      params.put("status", criteria.getStatus().toString());
    }

    if(criteria.getTransactionCreatedAt() != null){
      sql.append(" AND EXTRACT(YEAR FROM to_timestamp(t.created_at / 1000)) = :year");
      sql.append(" AND EXTRACT(MONTH FROM to_timestamp(t.created_at / 1000)) = :month");
      sql.append(" AND EXTRACT(DAY FROM to_timestamp(t.created_at / 1000)) = :day");

      params.put("year",criteria.getTransactionCreatedAt().getYear());
      params.put("month",criteria.getTransactionCreatedAt().getMonth());
      params.put("day",criteria.getTransactionCreatedAt().getDayOfMonth());
    }

    if(criteria.getTransactionCreatedAroundMonthAt() != null){
      sql.append(" AND EXTRACT(YEAR FROM to_timestamp(t.created_at / 1000)) = :year");
      sql.append(" AND EXTRACT(MONTH FROM to_timestamp(t.created_at / 1000)) = :month");

      params.put("year",criteria.getTransactionCreatedAt().getYear());
      params.put("month",criteria.getTransactionCreatedAt().getMonth());
    }

    // ===== Pagination =====
    sql.append(" LIMIT :pageSize OFFSET :offset");
    params.put("pageSize", criteria.getPageSize());
    params.put("offset", criteria.getOffset());
    DatabaseClient.GenericExecuteSpec spec = databaseClient.sql(sql.toString());
    for (var entry : params.entrySet()) {
      spec = spec.bind(entry.getKey(), entry.getValue());
    }
     return spec.map((row, meta) ->TransactionHistoryDTO.builder()
            .id(row.get("id", Long.class))
            .balance(row.get("transaction_balance",String.class))
            .sourceWalletId(row.get("source_wallet_id",Long.class))
            .destinationWalletId(row.get("beneficiary_wallet_id",Long.class))
            .description(row.get("description",String.class))
            .createdAt(row.get("created_at",Long.class))
            .methodName(row.get("method_name", String.class))
             .status(row.get("transaction_status",String.class))
             .type(row.get("transaction_type",String.class))
            .build()).all();
  }
}
