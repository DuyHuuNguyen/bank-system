package com.bank.wallet_service.api.facade;

import com.bank.wallet_service.api.request.CreateWalletRequest;
import com.bank.wallet_service.api.response.BaseResponse;
import com.bank.wallet_service.api.response.WalletResponse;
import java.util.List;
import reactor.core.publisher.Mono;

public interface WalletFacade {
  Mono<BaseResponse<Void>> createWallet(CreateWalletRequest request);

  Mono<BaseResponse<List<WalletResponse>>> findMyWallets();
}
