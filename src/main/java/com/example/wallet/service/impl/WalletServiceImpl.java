package com.example.wallet.service.impl;

import com.example.wallet.model.Wallet;
import com.example.wallet.repository.WalletRepository;
import com.example.wallet.service.WalletService;
import com.example.wallet.web.dto.WalletBalanceResponse;
import com.example.wallet.web.dto.WalletUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.ReentrantLock;


@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {
    private final WalletRepository walletRepository;

    private final ExecutorService executorService;
    private final ConcurrentHashMap<UUID, ReentrantLock> lockMap = new ConcurrentHashMap<>();


    @Override
    @Transactional
    @CacheEvict(cacheNames = "walletCache", key = "#request.walletId")
    public WalletBalanceResponse updateWallet(WalletUpdateRequest request) {
        UUID walletId = request.getWalletId();
        ReentrantLock lock = lockMap.computeIfAbsent(walletId, k -> new ReentrantLock());
        try (AutoCloseableLock autoCloseableLock = new AutoCloseableLock(lock)) {
            Wallet wallet = walletRepository.findById(walletId).orElseGet(() -> {
                Wallet newWallet = new Wallet();
                newWallet.setId(walletId);
                newWallet.setBalance(BigDecimal.ZERO);
                return newWallet;
            });
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
            asyncSaveWallet(wallet);
            return response;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Async
    private void asyncSaveWallet(Wallet wallet) {
        try {
            walletRepository.save(wallet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @Cacheable(cacheNames = "walletBalanceCache", key = "#walletId")
    @Async
    public CompletableFuture<WalletBalanceResponse> getWalletBalance(UUID walletId) {
        return CompletableFuture.supplyAsync(() -> {
            ReentrantLock lock = lockMap.computeIfAbsent(walletId, k -> new ReentrantLock());
            try (AutoCloseableLock autoCloseableLock = new AutoCloseableLock(lock)) {
                Wallet wallet = walletRepository.findById(walletId).orElse(null);
                if (wallet == null) {
                    return new WalletBalanceResponse("Wallet not found", null, HttpStatus.NOT_FOUND);
                }
                return new WalletBalanceResponse("Wallet balance retrieved successfully", wallet.getBalance(), HttpStatus.OK);
            } catch (Exception e) {
                return new WalletBalanceResponse("Error occurred", null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        });
    }
}

class AutoCloseableLock implements AutoCloseable {
    private final ReentrantLock lock;

    public AutoCloseableLock(ReentrantLock lock) {
        this.lock = lock;
        lock.lock();
    }

    @Override
    public void close() {
        lock.unlock();
    }
}