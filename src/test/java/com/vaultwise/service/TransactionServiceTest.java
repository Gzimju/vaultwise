package com.vaultwise.service;

import com.vaultwise.model.Account;
import com.vaultwise.model.Transaction;
import com.vaultwise.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTransaction() {
        // Create a mock account and a transaction with the account set
        Account account = new Account();
        account.setId(1L);  // Set the account ID or any necessary account fields
        Transaction transaction = new Transaction(new BigDecimal("100.00"), "Deposit", account);

        // Mock the repository's save method to return the transaction
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        // Call the createTransaction method
        Transaction createdTransaction = transactionService.createTransaction(transaction);

        // Assert that the transaction was created successfully
        assertNotNull(createdTransaction);
        assertEquals(new BigDecimal("100.00"), createdTransaction.getAmount());
        assertEquals("Deposit", createdTransaction.getType());
        assertEquals(account, createdTransaction.getAccount());  // Ensure the account is correctly set
        verify(transactionRepository, times(1)).save(any(Transaction.class));  // Verify save is called once
    }

    @Test
    void testGetAllTransactions() {
        List<Transaction> transactions = Arrays.asList(
                new Transaction(new BigDecimal("50.00"), "Withdrawal", null),
                new Transaction(new BigDecimal("200.00"), "Deposit", null)
        );

        when(transactionRepository.findAll()).thenReturn(transactions);

        List<Transaction> result = transactionService.getAllTransactions();

        assertEquals(2, result.size());
        assertEquals(new BigDecimal("50.00"), result.get(0).getAmount());
        assertEquals("Withdrawal", result.get(0).getType());
        verify(transactionRepository, times(1)).findAll();
    }

    @Test
    void testGetTransactionById() {
        Transaction transaction = new Transaction(new BigDecimal("75.00"), "Deposit", null);
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));

        Transaction result = transactionService.getTransactionById(1L);

        assertNotNull(result);
        assertEquals(new BigDecimal("75.00"), result.getAmount());
        assertEquals("Deposit", result.getType());
        verify(transactionRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateTransaction() {
        Transaction existingTransaction = new Transaction(new BigDecimal("100.00"), "Deposit", null);
        Transaction updatedTransaction = new Transaction(new BigDecimal("120.00"), "Deposit", null);

        when(transactionRepository.findById(1L)).thenReturn(Optional.of(existingTransaction));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(updatedTransaction);

        Transaction result = transactionService.updateTransaction(1L, updatedTransaction);

        assertNotNull(result);
        assertEquals(new BigDecimal("120.00"), result.getAmount());
        verify(transactionRepository, times(1)).findById(1L);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void testDeleteTransaction() {
        doNothing().when(transactionRepository).deleteById(1L);

        transactionService.deleteTransaction(1L);

        verify(transactionRepository, times(1)).deleteById(1L);
    }
}
