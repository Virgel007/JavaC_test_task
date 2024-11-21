package com.example.wallet;

import com.example.wallet.service.impl.WalletServiceImpl;
import com.example.wallet.web.controller.WalletController;
import com.example.wallet.web.dto.OperationType;
import com.example.wallet.web.dto.WalletBalanceResponse;
import com.example.wallet.web.dto.WalletUpdateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
class WalletControllerTest {

    @Mock
    private WalletServiceImpl walletService;
    @InjectMocks
    private WalletController walletController;
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        when(walletService.updateWallet(any(WalletUpdateRequest.class))).thenReturn(new WalletBalanceResponse("Wallet balance updated successfully", BigDecimal.valueOf(150), HttpStatus.OK));
        mockMvc = MockMvcBuilders.standaloneSetup(walletController).build();
    }

    @Test
    @DisplayName("Test get wallet balance")
    public void testGetWalletBalance() throws Exception {
        // Arrange
        UUID walletId = UUID.randomUUID();

        // Mock WalletController
        when(walletService.getWalletBalance(walletId)).thenReturn(CompletableFuture.completedFuture(new WalletBalanceResponse("Wallet balance", BigDecimal.valueOf(100.0), HttpStatus.OK)));

        // Act
        MvcResult result = mockMvc.perform(get("/api/v1/wallet/{walletId}", walletId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Wallet balance"))
                .andExpect(jsonPath("$.balance").value(100.0))
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andReturn();

        // Verify
        verify(walletService, times(1)).getWalletBalance(walletId);

        // Debug log
        System.out.println("Result: " + result.getModelAndView());
    }

    @Test
    @DisplayName("Test update wallet balance")
    public void testUpdateWalletBalance() throws Exception {
        // Arrange
        UUID walletId = UUID.randomUUID();

        WalletUpdateRequest request = new WalletUpdateRequest(walletId, OperationType.DEPOSIT, BigDecimal.valueOf(50));

        // Mock WalletService
        when(walletService.updateWallet(request)).thenReturn(new WalletBalanceResponse("Wallet balance updated successfully", BigDecimal.valueOf(150), HttpStatus.OK));

        // Act
        MvcResult result = mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Wallet balance updated successfully"))
                .andExpect(jsonPath("$.balance").value(150))
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andReturn();

        // Verify
        verify(walletService, times(1)).updateWallet(request);

        // Debug log
        System.out.println("Result: " + result.getModelAndView());
    }
}