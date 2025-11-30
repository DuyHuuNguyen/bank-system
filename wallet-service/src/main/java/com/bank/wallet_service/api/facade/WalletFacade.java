package com.bank.wallet_service.api.facade;

import com.bank.wallet_service.api.request.CreateWalletRequest;
import com.bank.wallet_service.api.request.DepositRequest;
import com.bank.wallet_service.api.request.TransferRequest;
import com.bank.wallet_service.api.request.WithDrawRequest;
import com.bank.wallet_service.api.response.*;
import java.util.List;
import reactor.core.publisher.Mono;

public interface WalletFacade {
  Mono<BaseResponse<Void>> createWallet(CreateWalletRequest request);

  Mono<BaseResponse<List<WalletResponse>>> findMyWallets();

  Mono<TransferResponse> transfer(TransferRequest request);

  Mono<DepositResponse> deposit(DepositRequest request);

  Mono<WithDrawResponse> withDraw(WithDrawRequest request);
}
