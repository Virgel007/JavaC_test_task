package com.example.wallet;

import com.example.wallet.model.Wallet;
import com.example.wallet.repository.WalletRepository;
import com.example.wallet.service.impl.WalletServiceImpl;
import com.example.wallet.web.dto.OperationType;
import com.example.wallet.web.dto.WalletBalanceResponse;
import com.example.wallet.web.dto.WalletUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private WalletServiceImpl walletService;

    private Wallet wallet;

    @BeforeEach
    void setup() {
        wallet = new Wallet();
        wallet.setId(UUID.randomUUID());
        wallet.setBalance(BigDecimal.valueOf(100));
    }

    @Test
    void testGetWalletBalance() {
        // Arrange
        when(walletRepository.findById(any(UUID.class))).thenReturn(Optional.of(wallet));

        // Act
        CompletableFuture<WalletBalanceResponse> response = walletService.getWalletBalance(wallet.getId());

        // Assert
        response.thenAccept(res -> assertEquals(BigDecimal.valueOf(100), res.getBalance()));
    }

    @Test
    void testUpdateWallet() {
        // Arrange
        when(walletRepository.findById(any(UUID.class))).thenReturn(Optional.of(wallet));
        when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);

        // Act
        WalletUpdateRequest request = new WalletUpdateRequest(wallet.getId(), OperationType.DEPOSIT, BigDecimal.valueOf(50));
        WalletBalanceResponse response = walletService.updateWallet(request);

        // Assert
        assertEquals(BigDecimal.valueOf(150), response.getBalance());
    }
}