package com.vaultwise.controller;

import com.vaultwise.dto.BankingRequest;
import com.vaultwise.model.Account;
import com.vaultwise.model.Card;
import com.vaultwise.model.Payment;
import com.vaultwise.service.BankingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class BankingControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BankingService bankingService;

    @InjectMocks
    private BankingController bankingController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bankingController).build();
    }

    @Test
    public void testCreateAccount() throws Exception {
        // Create mock data for the account
        Account account = new Account();
        account.setAccountNumber("12345");
        account.setBalance(BigDecimal.valueOf(1000));

        // When the banking service is called to create an account, return the mock account
        when(bankingService.createAccount(any(Account.class))).thenReturn(account);

        // Test the controller endpoint
        mockMvc.perform(post("/api/banking/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"accountNumber\":\"12345\",\"balance\":1000}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value("12345"))
                .andExpect(jsonPath("$.balance").value(1000));
    }

    @Test
    public void testDeposit() throws Exception {
        // Create a mock request for deposit
        BankingRequest bankingRequest = new BankingRequest();
        bankingRequest.setAccountNumber("12345");
        bankingRequest.setAmount(BigDecimal.valueOf(500));

        // Mock the banking service deposit method to do nothing
        doNothing().when(bankingService).deposit(anyString(), any(BigDecimal.class));

        // Test the deposit endpoint
        mockMvc.perform(post("/api/banking/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"accountNumber\":\"12345\",\"amount\":500}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Deposit successful"));

        // Verify that the banking service method was called once
        verify(bankingService, times(1)).deposit("12345", BigDecimal.valueOf(500));
    }

    @Test
    public void testWithdraw() throws Exception {
        // Create a mock request for withdraw
        BankingRequest bankingRequest = new BankingRequest();
        bankingRequest.setAccountNumber("12345");
        bankingRequest.setAmount(BigDecimal.valueOf(200));

        // Mock the banking service withdraw method to do nothing
        doNothing().when(bankingService).withdraw(anyString(), any(BigDecimal.class));

        // Test the withdraw endpoint
        mockMvc.perform(post("/api/banking/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"accountNumber\":\"12345\",\"amount\":200}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Withdrawal successful"));

        // Verify that the banking service method was called once
        verify(bankingService, times(1)).withdraw("12345", BigDecimal.valueOf(200));
    }

    @Test
    public void testGetCards() throws Exception {
        // Create a mock list of cards
        Card card = new Card();
        card.setCardNumber("1234567890");

        List<Card> cards = Arrays.asList(card);
        when(bankingService.getCards(1L)).thenReturn(cards);

        // Test the get cards endpoint
        mockMvc.perform(get("/api/banking/cards/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cardNumber").value("1234567890"));
    }

    @Test
    public void testGetPayments() throws Exception {
        // Create a mock list of payments
        Payment payment = new Payment();
        payment.setAmount(BigDecimal.valueOf(100));

        List<Payment> payments = Arrays.asList(payment);
        when(bankingService.getPayments(1L)).thenReturn(payments);

        // Test the get payments endpoint
        mockMvc.perform(get("/api/banking/payments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].amount").value(100));
    }
}
