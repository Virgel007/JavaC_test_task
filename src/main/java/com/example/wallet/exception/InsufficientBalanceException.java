package com.example.wallet.exception;

import lombok.Getter;

import java.math.BigDecimal;


@Getter
public class InsufficientBalanceException extends RuntimeException {

    private final BigDecimal balance;

    public InsufficientBalanceException(String message, BigDecimal balance) {
        super(message);
        this.balance = balance;
    }

}
