package com.vaultwise.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaultwise.model.Transaction;
import com.vaultwise.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TransactionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
    }

    @Test
    void testCreateTransaction() throws Exception {
        // If transactions are made automatically, this test may be unnecessary.
        Transaction transaction = new Transaction(new BigDecimal("100.00"), "Deposit", null);
        transaction.setTransactionDate(new Date());

        when(transactionService.createTransaction(any(Transaction.class))).thenReturn(transaction);

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transaction)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(100.00))
                .andExpect(jsonPath("$.type").value("Deposit"));
    }

    @Test
    void testGetAllTransactions() throws Exception {
        List<Transaction> transactions = Arrays.asList(
                new Transaction(new BigDecimal("50.00"), "Withdrawal", null),
                new Transaction(new BigDecimal("200.00"), "Deposit", null)
        );

        when(transactionService.getAllTransactions()).thenReturn(transactions);

        mockMvc.perform(get("/api/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].amount").value(50.00))
                .andExpect(jsonPath("$[1].amount").value(200.00));
    }

    @Test
    void testGetTransactionById() throws Exception {
        Transaction transaction = new Transaction(new BigDecimal("75.00"), "Deposit", null);
        when(transactionService.getTransactionById(1L)).thenReturn(transaction);

        mockMvc.perform(get("/api/transactions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(75.00))
                .andExpect(jsonPath("$.type").value("Deposit"));
    }

    @Test
    void testUpdateTransaction() throws Exception {
        Transaction updatedTransaction = new Transaction(new BigDecimal("120.00"), "Deposit", null);
        when(transactionService.updateTransaction(eq(1L), any(Transaction.class))).thenReturn(updatedTransaction);

        mockMvc.perform(put("/api/transactions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTransaction)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(120.00))
                .andExpect(jsonPath("$.type").value("Deposit"));
    }

    @Test
    void testDeleteTransaction() throws Exception {
        doNothing().when(transactionService).deleteTransaction(1L);

        mockMvc.perform(delete("/api/transactions/1"))
                .andExpect(status().isNoContent());
    }
}
