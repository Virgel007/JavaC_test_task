package com.example.wallet.service;

import com.example.wallet.web.dto.WalletBalanceResponse;
import com.example.wallet.web.dto.WalletUpdateRequest;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface WalletService {
    WalletBalanceResponse updateWallet(WalletUpdateRequest walletUpdateRequest);

    CompletableFuture<WalletBalanceResponse> getWalletBalance(UUID walletId);
}
