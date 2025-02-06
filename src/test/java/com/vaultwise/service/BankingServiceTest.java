package com.vaultwise.service;

import com.vaultwise.model.Account;
import com.vaultwise.model.Card;
import com.vaultwise.model.Payment;
import com.vaultwise.repository.AccountRepository;
import com.vaultwise.repository.CardRepository;
import com.vaultwise.repository.PaymentRepository;
import com.vaultwise.service.BankingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BankingServiceTest {

    private BankingService bankingService;
    private AccountRepository accountRepository;
    private CardRepository cardRepository;
    private PaymentRepository paymentRepository;

    @BeforeEach
    void setUp() {
        accountRepository = mock(AccountRepository.class);
        cardRepository = mock(CardRepository.class);
        paymentRepository = mock(PaymentRepository.class);
        bankingService = new BankingService(accountRepository, cardRepository, paymentRepository);
    }

    @Test
    void testCreateAccount() {
        Account account = new Account();
        account.setAccountNumber("12345");
        account.setBalance(BigDecimal.ZERO);

        when(accountRepository.findByAccountNumber(account.getAccountNumber())).thenReturn(Optional.empty());
        when(accountRepository.save(account)).thenReturn(account);

        Account savedAccount = bankingService.createAccount(account);

        assertNotNull(savedAccount);
        assertEquals("12345", savedAccount.getAccountNumber());
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void testCreateAccount_throwsIllegalArgumentException_whenAccountNumberExists() {
        Account account = new Account();
        account.setAccountNumber("12345");

        when(accountRepository.findByAccountNumber(account.getAccountNumber())).thenReturn(Optional.of(account));

        assertThrows(IllegalArgumentException.class, () -> bankingService.createAccount(account));
    }

    @Test
    void testDeposit() {
        String accountNumber = "12345";
        BigDecimal amount = new BigDecimal("100.00");
        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setBalance(new BigDecimal("50.00"));

        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(account));

        bankingService.deposit(accountNumber, amount);

        assertEquals(new BigDecimal("150.00"), account.getBalance());
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void testWithdraw() {
        String accountNumber = "12345";
        BigDecimal amount = new BigDecimal("50.00");
        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setBalance(new BigDecimal("100.00"));

        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(account));

        bankingService.withdraw(accountNumber, amount);

        assertEquals(new BigDecimal("50.00"), account.getBalance());
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void testWithdraw_throwsRuntimeException_whenInsufficientBalance() {
        String accountNumber = "12345";
        BigDecimal amount = new BigDecimal("150.00");
        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setBalance(new BigDecimal("100.00"));

        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(account));

        assertThrows(RuntimeException.class, () -> bankingService.withdraw(accountNumber, amount));
    }

    @Test
    void testUpdateAccount() {
        Long accountId = 1L;
        Account updatedAccount = new Account();
        updatedAccount.setAccountNumber("67890");
        updatedAccount.setBalance(new BigDecimal("200.00"));

        Account existingAccount = new Account();
        existingAccount.setAccountNumber("12345");
        existingAccount.setBalance(new BigDecimal("100.00"));
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(existingAccount));
        when(accountRepository.save(existingAccount)).thenReturn(existingAccount);

        Account result = bankingService.updateAccount(accountId, updatedAccount);

        assertNotNull(result);
        assertEquals("67890", result.getAccountNumber());
        assertEquals(new BigDecimal("200.00"), result.getBalance());
    }

    @Test
    void testGetCards() {
        Long accountId = 1L;
        Account account = new Account();
        account.setId(accountId);
        List<Card> cards = List.of(new Card(), new Card());

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(cardRepository.findByAccount(account)).thenReturn(cards);

        List<Card> result = bankingService.getCards(accountId);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testGetPayments() {
        Long accountId = 1L;
        Account account = new Account();
        account.setId(accountId);
        List<Payment> payments = List.of(new Payment(), new Payment());

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(paymentRepository.findByAccount(account)).thenReturn(payments);

        List<Payment> result = bankingService.getPayments(accountId);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testGetAccountByNumber() {
        String accountNumber = "12345";
        Account account = new Account();
        account.setAccountNumber(accountNumber);

        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(account));

        Account result = bankingService.getAccountByNumber(accountNumber);

        assertNotNull(result);
        assertEquals(accountNumber, result.getAccountNumber());
    }

    @Test
    void testDeleteAccount() {
        Long accountId = 1L;
        Account account = new Account();
        account.setId(accountId);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        bankingService.deleteAccount(accountId);

        verify(accountRepository, times(1)).delete(account);
    }
}
