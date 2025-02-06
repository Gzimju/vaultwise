package com.vaultwise.service;

import com.vaultwise.dto.PaymentRequest;
import com.vaultwise.model.Account;
import com.vaultwise.model.Payment;
import com.vaultwise.repository.AccountRepository;
import com.vaultwise.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
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
    private PaymentRequest paymentRequest;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setAccountNumber("12345");
        account.setBalance(BigDecimal.valueOf(1000));

        paymentRequest = new PaymentRequest();
        paymentRequest.setAccountNumber("12345");
        paymentRequest.setAmount(BigDecimal.valueOf(100));
        paymentRequest.setDescription("Test Payment");
    }

    @Test
    void testCreatePayment() {
        // Given
        when(accountRepository.findByAccountNumber("12345")).thenReturn(Optional.of(account));
        when(paymentRepository.save(Mockito.any(Payment.class))).thenReturn(new Payment(BigDecimal.valueOf(100), "Test Payment", account));

        // When
        Payment payment = paymentService.createPayment("12345", BigDecimal.valueOf(100), "Test Payment");

        // Then
        assertNotNull(payment);
        assertEquals("Test Payment", payment.getDescription());
        assertEquals(BigDecimal.valueOf(100), payment.getAmount());
        assertEquals(account, payment.getAccount());
    }

    @Test
    void testGetAllPayments() {
        // Given
        Payment payment1 = new Payment(BigDecimal.valueOf(100), "Payment 1", account);
        Payment payment2 = new Payment(BigDecimal.valueOf(200), "Payment 2", account);
        when(paymentRepository.findAll()).thenReturn(Arrays.asList(payment1, payment2));

        // When
        List<Payment> payments = paymentService.getAllPayments();

        // Then
        assertNotNull(payments);
        assertEquals(2, payments.size());
        assertEquals("Payment 1", payments.get(0).getDescription());
        assertEquals("Payment 2", payments.get(1).getDescription());
    }

    // New test: Get a payment by ID
    @Test
    void testGetPaymentById() {
        // Given
        Payment payment = new Payment(BigDecimal.valueOf(100), "Test Payment", account);
        payment.setId(1L);
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));

        // When
        Payment foundPayment = paymentService.getPaymentById(1L);

        // Then
        assertNotNull(foundPayment);
        assertEquals("Test Payment", foundPayment.getDescription());
        assertEquals(BigDecimal.valueOf(100), foundPayment.getAmount());
    }


    @Test
    void testUpdatePayment() {
        // Given
        Payment existingPayment = new Payment(BigDecimal.valueOf(50), "Old Payment", account);
        existingPayment.setId(1L);

        when(paymentRepository.findById(1L)).thenReturn(Optional.of(existingPayment));
        when(paymentRepository.save(Mockito.any(Payment.class))).thenReturn(existingPayment);

        // When
        Payment updatedPayment = paymentService.updatePayment(1L, paymentRequest);

        // Then
        assertNotNull(updatedPayment);
        assertEquals(BigDecimal.valueOf(100), updatedPayment.getAmount());
        assertEquals("Test Payment", updatedPayment.getDescription());
    }

    @Test
    void testDeletePayment() {
        // Given
        when(paymentRepository.existsById(1L)).thenReturn(true);

        // When & Then
        assertDoesNotThrow(() -> paymentService.deletePayment(1L));

        // Verify that deleteById was called
        verify(paymentRepository, times(1)).deleteById(1L);
    }

}
