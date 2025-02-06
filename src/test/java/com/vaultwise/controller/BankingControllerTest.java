package com.vaultwise.controller;

import com.vaultwise.controller.BankingController;
import com.vaultwise.dto.BankingRequest;
import com.vaultwise.model.Account;
import com.vaultwise.model.Card;
import com.vaultwise.model.Payment;
import com.vaultwise.service.BankingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BankingControllerTest {

    private BankingController bankingController;
    private BankingService bankingService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        bankingService = mock(BankingService.class);
        bankingController = new BankingController(bankingService);
        mockMvc = MockMvcBuilders.standaloneSetup(bankingController).build();
    }

    @Test
    void testCreateAccount() throws Exception {
        Account account = new Account();
        account.setAccountNumber("12345");
        account.setBalance(BigDecimal.ZERO);

        when(bankingService.createAccount(any(Account.class))).thenReturn(account);

        mockMvc.perform(post("/api/banking/create")
                        .contentType("application/json")
                        .content("{\"accountNumber\":\"12345\",\"balance\":0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value("12345"));
    }

    @Test
    void testDeposit() throws Exception {
        BankingRequest bankingRequest = new BankingRequest();
        bankingRequest.setAccountNumber("12345");
        bankingRequest.setAmount(new BigDecimal("100.00"));

        doNothing().when(bankingService).deposit(anyString(), any(BigDecimal.class));

        mockMvc.perform(post("/api/banking/deposit")
                        .contentType("application/json")
                        .content("{\"accountNumber\":\"12345\",\"amount\":100}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Deposit successful"));
    }

    @Test
    void testWithdraw() throws Exception {
        BankingRequest bankingRequest = new BankingRequest();
        bankingRequest.setAccountNumber("12345");
        bankingRequest.setAmount(new BigDecimal("50.00"));

        doNothing().when(bankingService).withdraw(anyString(), any(BigDecimal.class));

        mockMvc.perform(post("/api/banking/withdraw")
                        .contentType("application/json")
                        .content("{\"accountNumber\":\"12345\",\"amount\":50}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Withdrawal successful"));
    }

    @Test
    void testGetCards() throws Exception {
        Long accountId = 1L;
        List<Card> cards = List.of(new Card(), new Card());

        when(bankingService.getCards(accountId)).thenReturn(cards);

        mockMvc.perform(get("/api/banking/cards/{accountId}", accountId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    void testGetPayments() throws Exception {
        Long accountId = 1L;
        List<Payment> payments = List.of(new Payment(), new Payment());

        when(bankingService.getPayments(accountId)).thenReturn(payments);

        mockMvc.perform(get("/api/banking/payments/{accountId}", accountId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    void testGetAccountByNumber() throws Exception {
        String accountNumber = "12345";
        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setBalance(BigDecimal.ZERO);

        when(bankingService.getAccountByNumber(accountNumber)).thenReturn(account);

        mockMvc.perform(get("/api/banking/account/{accountNumber}", accountNumber))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value(accountNumber));
    }

    @Test
    void testGetAccountById() throws Exception {
        Long accountId = 1L;
        Account account = new Account();
        account.setId(accountId);
        account.setBalance(BigDecimal.ZERO);

        when(bankingService.getAccountById(accountId)).thenReturn(account);

        mockMvc.perform(get("/api/banking/account/id/{accountId}", accountId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(accountId));
    }

    @Test
    void testGetAllAccounts() throws Exception {
        List<Account> accounts = List.of(new Account(), new Account());

        when(bankingService.getAllAccounts()).thenReturn(accounts);

        mockMvc.perform(get("/api/banking"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    void testDeleteAccount() throws Exception {
        Long accountId = 1L;

        doNothing().when(bankingService).deleteAccount(accountId);

        mockMvc.perform(delete("/api/banking/accounts/{accountId}", accountId))
                .andExpect(status().isOk())
                .andExpect(content().string("Account deleted successfully!"));
    }

    @Test
    void testUpdateAccount() throws Exception {
        Long accountId = 1L;
        Account updatedAccount = new Account();
        updatedAccount.setId(accountId);
        updatedAccount.setAccountNumber("12345");
        updatedAccount.setBalance(BigDecimal.valueOf(500));

        when(bankingService.updateAccount(eq(accountId), Mockito.any(Account.class))).thenReturn(updatedAccount);

        mockMvc.perform(put("/api/banking/accounts/{accountId}", accountId)
                        .contentType("application/json")
                        .content("{\"accountNumber\":\"12345\",\"balance\":500}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(500));
    }
}
