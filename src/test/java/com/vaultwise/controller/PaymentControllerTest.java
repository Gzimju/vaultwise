package com.vaultwise.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaultwise.dto.PaymentRequest;
import com.vaultwise.model.Payment;
import com.vaultwise.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(paymentController).build();
    }

    @Test
    void testCreatePayment() throws Exception {
        PaymentRequest request = new PaymentRequest();
        request.setAccountNumber("123456789");
        request.setAmount(new BigDecimal("100.00"));
        request.setDescription("Test Payment");

        Payment payment = new Payment();
        payment.setAmount(request.getAmount());
        payment.setDescription(request.getDescription());

        when(paymentService.createPayment(any(String.class), any(BigDecimal.class), any(String.class)))
                .thenReturn(payment);

        mockMvc.perform(post("/api/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
