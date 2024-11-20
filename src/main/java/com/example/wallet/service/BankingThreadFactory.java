package com.example.wallet.service;

import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadFactory;
@Component
public class BankingThreadFactory implements ThreadFactory {
    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setName("BankingThread");
        thread.setPriority(Thread.NORM_PRIORITY);
        return thread;
    }
}
