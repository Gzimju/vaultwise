package com.vaultwise.service;

import com.vaultwise.model.Account;
import com.vaultwise.model.Transaction;
import com.vaultwise.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private BankingService bankingService;

    @InjectMocks
    private TransactionService transactionService;

    private Transaction transaction;
    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setId(1L);

        transaction = new Transaction();
        transaction.setId(1L);
        transaction.setAmount(new BigDecimal("100.00"));
        transaction.setType("Deposit");
        transaction.setAccount(account);
    }

    @Test
    void testCreateTransaction_WithExistingAccount() {
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        Transaction savedTransaction = transactionService.createTransaction(transaction);

        assertNotNull(savedTransaction);
        assertEquals("Deposit", savedTransaction.getType());
        assertEquals(new BigDecimal("100.00"), savedTransaction.getAmount());
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    void testCreateTransaction_WithAccountId() {
        transaction.setAccount(null);
        transaction.setAccountId(1L);
        when(bankingService.getAccountById(1L)).thenReturn(account);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        Transaction savedTransaction = transactionService.createTransaction(transaction);

        assertNotNull(savedTransaction);
        assertEquals(account, savedTransaction.getAccount());
        verify(bankingService, times(1)).getAccountById(1L);
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    void testCreateTransaction_AccountNotFound() {
        transaction.setAccount(null);
        transaction.setAccountId(2L);
        when(bankingService.getAccountById(2L)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> transactionService.createTransaction(transaction));
        assertEquals("Account not found with ID: 2", exception.getMessage());
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void testGetAllTransactions() {
        List<Transaction> transactions = Arrays.asList(transaction);
        when(transactionRepository.findAll()).thenReturn(transactions);

        List<Transaction> retrievedTransactions = transactionService.getAllTransactions();

        assertFalse(retrievedTransactions.isEmpty());
        assertEquals(1, retrievedTransactions.size());
        verify(transactionRepository, times(1)).findAll();
    }

    @Test
    void testGetTransactionById_Success() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));

        Transaction retrievedTransaction = transactionService.getTransactionById(1L);

        assertNotNull(retrievedTransaction);
        assertEquals(1L, retrievedTransaction.getId());
        verify(transactionRepository, times(1)).findById(1L);
    }

    @Test
    void testGetTransactionById_NotFound() {
        when(transactionRepository.findById(2L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> transactionService.getTransactionById(2L));
        assertEquals("Transaction not found with ID: 2", exception.getMessage());
    }

    @Test
    void testDeleteTransaction() {
        doNothing().when(transactionRepository).deleteById(1L);

        transactionService.deleteTransaction(1L);

        verify(transactionRepository, times(1)).deleteById(1L);
    }
}
