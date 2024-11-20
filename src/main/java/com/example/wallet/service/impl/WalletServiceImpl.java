package com.example.wallet.service.impl;

import com.example.wallet.exception.InsufficientBalanceException;
import com.example.wallet.model.Wallet;
import com.example.wallet.repository.WalletRepository;
import com.example.wallet.service.WalletService;
import org.springframework.http.HttpStatus;
import com.example.wallet.web.dto.WalletBalanceResponse;
import com.example.wallet.web.dto.WalletUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;

    @Override
    public WalletBalanceResponse updateWallet(WalletUpdateRequest request) {
        Wallet wallet = walletRepository.findById(request.getWalletId()).orElse(null);
        if (wallet == null) {
            wallet = new Wallet();
            wallet.setId(request.getWalletId());
            wallet.setBalance(BigDecimal.ZERO);
        }
        WalletBalanceResponse response;
        switch (request.getOperationType()) {
            case DEPOSIT:
                wallet.setBalance(wallet.getBalance().add(request.getAmount()));
                response = new WalletBalanceResponse("Deposit successful", wallet.getBalance(), HttpStatus.OK);
                break;
            case WITHDRAW:
                BigDecimal balance = wallet.getBalance();
                BigDecimal newBalance = wallet.getBalance().subtract(request.getAmount());
                if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
                    response = new WalletBalanceResponse("Insufficient balance", balance, HttpStatus.BAD_REQUEST);
                } else {
                    wallet.setBalance(newBalance);
                    response = new WalletBalanceResponse("Withdrawal successful", wallet.getBalance(), HttpStatus.OK);
                }
                break;
            default:
                response = new WalletBalanceResponse("Unsupported operation type", null, HttpStatus.BAD_REQUEST);
                break;
        }
        walletRepository.save(wallet);
        return response;
    }

    @Override
    public WalletBalanceResponse getWalletBalance(UUID walletId) {
        Wallet wallet = walletRepository.findById(walletId).orElse(null);
        if (wallet == null) {
            return new WalletBalanceResponse("Wallet not found", null, HttpStatus.NOT_FOUND);
        }
        return new WalletBalanceResponse("Wallet balance retrieved successfully", wallet.getBalance(), HttpStatus.OK);    }
}
