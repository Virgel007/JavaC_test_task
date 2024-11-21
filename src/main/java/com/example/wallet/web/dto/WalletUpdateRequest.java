package com.example.wallet.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletUpdateRequest {
    private UUID walletId;
    private OperationType operationType;
    private BigDecimal amount;
}
