package com.vaultwise.service;

import com.vaultwise.model.Account;
import com.vaultwise.model.Transaction;
import com.vaultwise.repository.AccountRepository;
import com.vaultwise.repository.CardRepository;
import com.vaultwise.repository.PaymentRepository;
import com.vaultwise.repository.TransactionRepository;
import com.vaultwise.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class BankingServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CardRepository cardRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private BankingService bankingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Test
    void testCreateAccount() {
        // Given
        Account account = new Account();
        account.setAccountNumber("123456789");
        account.setBalance(BigDecimal.ZERO);

        when(accountRepository.findByAccountNumber(account.getAccountNumber())).thenReturn(Optional.empty());
        when(accountRepository.save(account)).thenReturn(account);

        // When
        Account createdAccount = bankingService.createAccount(account);

        // Then
        verify(accountRepository, times(1)).save(account);
        assertEquals("123456789", createdAccount.getAccountNumber());
        assertEquals(BigDecimal.ZERO, createdAccount.getBalance());
    }

    @Test
    void testDeposit() {
        // Given
        Account account = new Account();
        account.setAccountNumber("123456789");
        account.setBalance(new BigDecimal("100"));

        when(accountRepository.findByAccountNumber(account.getAccountNumber())).thenReturn(Optional.of(account));

        // Mock the return of createTransaction() assuming it returns a Transaction
        when(transactionService.createTransaction(any())).thenReturn(new Transaction());  // Mock a new transaction return

        // When
        bankingService.deposit(account.getAccountNumber(), new BigDecimal("50"));

        // Then
        verify(accountRepository, times(1)).save(account);
        assertEquals(new BigDecimal("150"), account.getBalance());
    }

    @Test
    void testWithdraw() {
        // Given
        Account account = new Account();
        account.setAccountNumber("123456789");
        account.setBalance(new BigDecimal("200"));

        when(accountRepository.findByAccountNumber(account.getAccountNumber())).thenReturn(Optional.of(account));

        // Mock the return of createTransaction() assuming it returns a Transaction
        when(transactionService.createTransaction(any())).thenReturn(new Transaction());  // Mock a new transaction return

        // When
        bankingService.withdraw(account.getAccountNumber(), new BigDecimal("50"));

        // Then
        verify(accountRepository, times(1)).save(account);
        assertEquals(new BigDecimal("150"), account.getBalance());
    }

    @Test
    void testGetAccountById() {
        // Given
        Account account = new Account();
        account.setId(1L);
        account.setAccountNumber("123456789");

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        // When
        Account fetchedAccount = bankingService.getAccountById(1L);

        // Then
        verify(accountRepository, times(1)).findById(1L);
        assertEquals("123456789", fetchedAccount.getAccountNumber());
    }

    @Test
    void testGetAccountByNumber() {
        // Given
        Account account = new Account();
        account.setAccountNumber("987654321");

        when(accountRepository.findByAccountNumber("987654321")).thenReturn(Optional.of(account));

        // When
        Account fetchedAccount = bankingService.getAccountByNumber("987654321");

        // Then
        verify(accountRepository, times(1)).findByAccountNumber("987654321");
        assertEquals("987654321", fetchedAccount.getAccountNumber());
    }

    @Test
    void testUpdateAccount() {
        // Given
        Account existingAccount = new Account();
        existingAccount.setId(1L);
        existingAccount.setAccountNumber("123456789");
        existingAccount.setBalance(new BigDecimal("100"));

        Account updatedAccount = new Account();
        updatedAccount.setAccountNumber("987654321");
        updatedAccount.setBalance(new BigDecimal("200"));

        when(accountRepository.findById(1L)).thenReturn(Optional.of(existingAccount));
        when(accountRepository.save(existingAccount)).thenReturn(existingAccount);

        // When
        Account updated = bankingService.updateAccount(1L, updatedAccount);

        // Then
        verify(accountRepository, times(1)).save(existingAccount);
        assertEquals("987654321", updated.getAccountNumber());
        assertEquals(new BigDecimal("200"), updated.getBalance());
    }

    @Test
    void testDeleteAccount() {
        // Given
        Account account = new Account();
        account.setId(1L);
        account.setAccountNumber("123456789");

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        doNothing().when(accountRepository).delete(account);

        // When
        bankingService.deleteAccount(1L);

        // Then
        verify(accountRepository, times(1)).delete(account);
    }

    @Test
    void testGetCards() {
        // Given
        Account account = new Account();
        account.setId(1L);
        account.setAccountNumber("123456789");

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(cardRepository.findByAccount(account)).thenReturn(null); // Mock empty list of cards

        // When
        var cards = bankingService.getCards(1L);

        // Then
        verify(cardRepository, times(1)).findByAccount(account);
        assertNull(cards); // Assumes no cards are found
    }

    @Test
    void testGetPayments() {
        // Given
        Account account = new Account();
        account.setId(1L);
        account.setAccountNumber("123456789");

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(paymentRepository.findByAccount(account)).thenReturn(null); // Mock empty list of payments

        // When
        var payments = bankingService.getPayments(1L);

        // Then
        verify(paymentRepository, times(1)).findByAccount(account);
        assertNull(payments); // Assumes no payments are found
    }
}
