package com.bank.wallet_service.domain.repository;

import com.bank.wallet_service.application.dto.WalletCurrencyDTO;
import com.bank.wallet_service.domain.entity.Wallet;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Repository
public interface WalletRepository extends R2dbcRepository<Wallet, Long> {
    @Query("""
   select 
       w.id
       ,w.available_balance 
       ,w.is_default
       ,c.currency_name
       ,w.user_id
,w.version
   from wallets w
   join currencies c
   on w.currency_id  = c.id
   where w.user_id =:userId
    """)
    Flux<WalletCurrencyDTO> findAllByUserId(Long userId);

    @Query("""
   select 
       w.id
       ,w.available_balance 
       ,w.is_default
       ,c.currency_name
       ,w.user_id
,w.version
   from wallets w
   join currencies c
   on w.currency_id  = c.id
   where w.user_id =:userId and w.id =:walletId
    """)
    Mono<WalletCurrencyDTO> findByUserIdAndWalletId(Long userId,Long walletId);

    @Query("""
   select 
       w.id
       ,w.available_balance 
       ,w.is_default
       ,c.currency_name
       ,w.user_id
,w.version
   from wallets w
   join currencies c
   on w.currency_id  = c.id
   where w.id =:id
    """)
    Mono<WalletCurrencyDTO> findWalletById(Long id);



    @Modifying
    @Query("""
    update wallets  
    set  spending_balance = spending_balance + abs(:amount)
          ,available_balance = available_balance - abs(:amount) 
          ,version = version + 1  
    where id =:id and  version =:version and available_balance > abs(:amount)
    """)
    Mono<Integer> subBalanceWallet(Long id, BigDecimal amount,Long version);

    @Modifying
    @Query("""
    update wallets  
    set   available_balance = available_balance + abs(:amount) 
          ,version = version + 1  
    where id =:id and  version =:version
    """)
    Mono<Integer> addBalanceWallet(Long id, BigDecimal amount,Long version);
}
