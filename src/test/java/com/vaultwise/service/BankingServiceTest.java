package com.vaultwise.service;

import com.vaultwise.model.Account;
import com.vaultwise.model.Card;
import com.vaultwise.model.Payment;
import com.vaultwise.repository.AccountRepository;
import com.vaultwise.repository.CardRepository;
import com.vaultwise.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BankingServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CardRepository cardRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private BankingService bankingService;

    // Test for creating a new account
    @Test
    public void testCreateAccount() {
        Account account = new Account();
        account.setAccountNumber("12345");
        account.setBalance(BigDecimal.valueOf(1000));

        // Mock repository methods
        when(accountRepository.save(account)).thenReturn(account);

        // Call service method
        Account createdAccount = bankingService.createAccount(account);

        // Assertions
        assertNotNull(createdAccount);
        assertEquals("12345", createdAccount.getAccountNumber());
        assertEquals(BigDecimal.valueOf(1000), createdAccount.getBalance());

        // Verify repository interaction
        verify(accountRepository, times(1)).save(account);
    }

    // Test for creating an account with null balance
    @Test
    public void testCreateAccountWithNullBalance() {
        Account account = new Account();
        account.setAccountNumber("12345");

        // Mock repository methods
        when(accountRepository.save(account)).thenReturn(account);

        // Call service method
        Account createdAccount = bankingService.createAccount(account);

        // Assertions
        assertNotNull(createdAccount);
        assertEquals(BigDecimal.ZERO, createdAccount.getBalance());  // Default balance

        // Verify repository interaction
        verify(accountRepository, times(1)).save(account);
    }

    // Test for depositing money into an account
    @Test
    public void testDeposit() {
        Account account = new Account();
        account.setAccountNumber("12345");
        account.setBalance(BigDecimal.valueOf(1000));

        // Mock repository methods
        when(accountRepository.findByAccountNumber("12345")).thenReturn(Optional.of(account));

        // Call service method
        bankingService.deposit("12345", BigDecimal.valueOf(500));

        // Assertions
        assertEquals(BigDecimal.valueOf(1500), account.getBalance());

        // Verify repository interaction
        verify(accountRepository, times(1)).findByAccountNumber("12345");
        verify(accountRepository, times(1)).save(account);
    }

    // Test for withdrawing money from an account
    @Test
    public void testWithdraw() {
        Account account = new Account();
        account.setAccountNumber("12345");
        account.setBalance(BigDecimal.valueOf(1000));

        // Mock repository methods
        when(accountRepository.findByAccountNumber("12345")).thenReturn(Optional.of(account));

        // Call service method
        bankingService.withdraw("12345", BigDecimal.valueOf(500));

        // Assertions
        assertEquals(BigDecimal.valueOf(500), account.getBalance());

        // Verify repository interaction
        verify(accountRepository, times(1)).findByAccountNumber("12345");
        verify(accountRepository, times(1)).save(account);
    }

    // Test for insufficient funds during withdrawal
    @Test
    public void testWithdrawInsufficientFunds() {
        Account account = new Account();
        account.setAccountNumber("12345");
        account.setBalance(BigDecimal.valueOf(100));

        // Mock repository methods
        when(accountRepository.findByAccountNumber("12345")).thenReturn(Optional.of(account));

        // Call service method and assert exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> bankingService.withdraw("12345", BigDecimal.valueOf(200)));
        assertEquals("Insufficient balance", exception.getMessage());

        // Verify repository interaction
        verify(accountRepository, times(1)).findByAccountNumber("12345");
        verify(accountRepository, times(0)).save(account);
    }

    // Test for getting cards by accountId
    @Test
    public void testGetCards() {
        Account account = new Account();
        account.setId(1L);
        account.setAccountNumber("12345");
        account.setBalance(BigDecimal.valueOf(1000));

        Card card = new Card();
        card.setId(1L);
        card.setAccount(account);

        // Mock repository methods
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(cardRepository.findByAccount(account)).thenReturn(Collections.singletonList(card));

        // Call service method
        List<Card> cards = bankingService.getCards(1L);

        // Assertions
        assertNotNull(cards);
        assertEquals(1, cards.size());
        assertEquals(account, cards.get(0).getAccount());

        // Verify repository interactions
        verify(accountRepository, times(1)).findById(1L);
        verify(cardRepository, times(1)).findByAccount(account);
    }

    // Test for getting payments by accountId
    @Test
    public void testGetPayments() {
        Account account = new Account();
        account.setId(1L);
        account.setAccountNumber("12345");
        account.setBalance(BigDecimal.valueOf(1000));

        Payment payment = new Payment();
        payment.setId(1L);
        payment.setAmount(BigDecimal.valueOf(100));
        payment.setAccount(account);

        // Mock repository methods
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(paymentRepository.findByAccount(account)).thenReturn(Collections.singletonList(payment));

        // Call service method
        List<Payment> payments = bankingService.getPayments(1L);

        // Assertions
        assertNotNull(payments);
        assertEquals(1, payments.size());
        assertEquals(BigDecimal.valueOf(100), payments.get(0).getAmount());

        // Verify repository interactions
        verify(accountRepository, times(1)).findById(1L);
        verify(paymentRepository, times(1)).findByAccount(account);
    }
}
