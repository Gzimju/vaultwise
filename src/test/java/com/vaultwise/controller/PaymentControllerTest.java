package com.vaultwise.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaultwise.dto.PaymentRequest;
import com.vaultwise.model.Account;
import com.vaultwise.model.Payment;
import com.vaultwise.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class PaymentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(paymentController).build();
    }

    @Test
    public void testCreatePayment() throws Exception {
        // Prepare the PaymentRequest
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setAccountNumber("123456");
        paymentRequest.setAmount(new BigDecimal("100.00"));
        paymentRequest.setDescription("Payment description");

        // Mocking the behavior of the paymentService to return a Payment object with a linked Account
        Account mockAccount = new Account();  // Assume Account entity exists with at least accountNumber field
        mockAccount.setAccountNumber("123456");

        Payment mockPayment = new Payment();
        mockPayment.setAccount(mockAccount);
        mockPayment.setAmount(new BigDecimal("100.00"));
        mockPayment.setDescription("Payment description");

        // Mocking the paymentService.createPayment method
        when(paymentService.createPayment(paymentRequest.getAccountNumber(), paymentRequest.getAmount(), paymentRequest.getDescription()))
                .thenReturn(mockPayment);

        // Perform the POST request to create the payment
        mockMvc.perform(post("/api/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(paymentRequest)))
                .andExpect(status().isOk())  // Expecting 200 OK
                .andExpect(jsonPath("$.account.accountNumber").value("123456"))  // Check accountNumber inside account object
                .andExpect(jsonPath("$.amount").value(100.00))
                .andExpect(jsonPath("$.description").value("Payment description"));
    }


    @Test
    public void testGetAllPayments() throws Exception {
        List<Payment> payments = List.of(
                new Payment(BigDecimal.valueOf(1000), "Test Payment 1", null),
                new Payment(BigDecimal.valueOf(1500), "Test Payment 2", null)
        );

        when(paymentService.getAllPayments()).thenReturn(payments);

        mockMvc.perform(get("/api/payment"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].amount").value(1000))
                .andExpect(jsonPath("$[1].amount").value(1500));
    }

    @Test
    public void testGetPaymentById() throws Exception {
        Long paymentId = 1L;
        Payment payment = new Payment(BigDecimal.valueOf(1000), "Test Payment", null);

        when(paymentService.getPaymentById(paymentId)).thenReturn(payment);

        mockMvc.perform(get("/api/payment/{id}", paymentId))
                .andExpect(status().isOk())  // 200 is expected
                .andExpect(jsonPath("$.amount").value(1000))
                .andExpect(jsonPath("$.description").value("Test Payment"));
    }

    @Test
    public void testUpdatePayment() throws Exception {
        Long paymentId = 1L;
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setAccountNumber("12345");
        paymentRequest.setAmount(BigDecimal.valueOf(1500));
        paymentRequest.setDescription("Updated Payment");

        Payment updatedPayment = new Payment(BigDecimal.valueOf(1500), "Updated Payment", null);

        when(paymentService.updatePayment(eq(paymentId), any())).thenReturn(updatedPayment);

        mockMvc.perform(put("/api/payment/{id}", paymentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(paymentRequest)))
                .andExpect(status().isOk())  // 200 is expected
                .andExpect(jsonPath("$.amount").value(1500))
                .andExpect(jsonPath("$.description").value("Updated Payment"));
    }

    @Test
    public void testDeletePayment() throws Exception {
        Long paymentId = 1L;

        doNothing().when(paymentService).deletePayment(paymentId);

        mockMvc.perform(delete("/api/payment/{id}", paymentId))
                .andExpect(status().isNoContent());  // 204 is expected as there is no content in the response
    }
}
