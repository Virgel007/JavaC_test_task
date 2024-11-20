package com.example.wallet.web.controller;

import com.example.wallet.service.WalletService;
import com.example.wallet.web.dto.WalletBalanceResponse;
import com.example.wallet.web.dto.WalletUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/wallet")
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;

    @PostMapping
    public ResponseEntity<WalletBalanceResponse> updateWallet(@RequestBody WalletUpdateRequest request) {
        WalletBalanceResponse response = walletService.updateWallet(request);
        return ResponseEntity.status(response.getHttpStatus().value()).body(response);
    }

    @GetMapping("/{walletId}")
    public ResponseEntity<WalletBalanceResponse> getWalletBalance(@PathVariable UUID walletId) {
        CompletableFuture<WalletBalanceResponse> future = walletService.getWalletBalance(walletId);
        WalletBalanceResponse response = future.join();
        return ResponseEntity.status(response.getHttpStatus().value()).body(response);
    }
}
