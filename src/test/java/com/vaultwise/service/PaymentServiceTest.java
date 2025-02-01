package com.vaultwise.service;

import com.vaultwise.model.Account;
import com.vaultwise.model.Payment;
import com.vaultwise.repository.AccountRepository;
import com.vaultwise.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setAccountNumber("123456789");
    }

    @Test
    void testCreatePayment_Success() {
        BigDecimal amount = new BigDecimal("100.00");
        String description = "Test Payment";

        when(accountRepository.findByAccountNumber(any(String.class))).thenReturn(Optional.of(account));
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment payment = paymentService.createPayment("123456789", amount, description);

        assertNotNull(payment);
        assertEquals(amount, payment.getAmount());
        assertEquals(description, payment.getDescription());
        assertEquals(account, payment.getAccount());
    }

    @Test
    void testCreatePayment_AccountNotFound() {
        when(accountRepository.findByAccountNumber(any(String.class))).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                paymentService.createPayment("123456789", new BigDecimal("100.00"), "Test Payment")
        );

        assertEquals("Account not found", exception.getMessage());
        verify(paymentRepository, never()).save(any(Payment.class));
    }
}
