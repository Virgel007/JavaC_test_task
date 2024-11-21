package com.example.wallet.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletBalanceResponse {
    private String message;
    private BigDecimal balance;
    private HttpStatus httpStatus;
}
