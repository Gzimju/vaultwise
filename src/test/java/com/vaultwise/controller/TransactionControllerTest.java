package com.vaultwise.controller;

import com.vaultwise.model.Transaction;
import com.vaultwise.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    private Transaction transaction;

    @BeforeEach
    void setUp() {
        transaction = new Transaction();
        transaction.setId(1L);
        transaction.setAmount(new BigDecimal("100.00"));
        transaction.setType("Deposit");
    }

    @Test
    void testCreateTransaction() {
        when(transactionService.createTransaction(any(Transaction.class))).thenReturn(transaction);

        ResponseEntity<Transaction> response = transactionController.createTransaction(transaction);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        verify(transactionService, times(1)).createTransaction(transaction);
    }

    @Test
    void testGetAllTransactions() {
        List<Transaction> transactions = Arrays.asList(transaction);
        when(transactionService.getAllTransactions()).thenReturn(transactions);

        ResponseEntity<List<Transaction>> response = transactionController.getAllTransactions();

        assertNotNull(response);
        assertFalse(response.getBody().isEmpty());
        assertEquals(1, response.getBody().size());
        verify(transactionService, times(1)).getAllTransactions();
    }

    @Test
    void testGetTransactionById_Success() {
        when(transactionService.getTransactionById(1L)).thenReturn(transaction);

        ResponseEntity<Transaction> response = transactionController.getTransactionById(1L);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        verify(transactionService, times(1)).getTransactionById(1L);
    }

    @Test
    void testGetTransactionById_NotFound() {
        when(transactionService.getTransactionById(2L)).thenThrow(new RuntimeException("Transaction not found with ID: 2"));

        Exception exception = assertThrows(RuntimeException.class, () -> transactionController.getTransactionById(2L));
        assertEquals("Transaction not found with ID: 2", exception.getMessage());
    }

    @Test
    void testDeleteTransaction() {
        doNothing().when(transactionService).deleteTransaction(1L);

        ResponseEntity<Void> response = transactionController.deleteTransaction(1L);

        assertNotNull(response);
        assertEquals(204, response.getStatusCodeValue());
        verify(transactionService, times(1)).deleteTransaction(1L);
    }
}
