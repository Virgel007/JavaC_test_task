package com.example.wallet.service;

import com.example.wallet.web.dto.WalletBalanceResponse;
import com.example.wallet.web.dto.WalletUpdateRequest;

import java.util.UUID;

public interface WalletService {
    WalletBalanceResponse updateWallet(WalletUpdateRequest walletUpdateRequest);

    WalletBalanceResponse getWalletBalance(UUID walletId);
}
