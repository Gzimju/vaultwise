package com.vaultwise.controller;

import com.vaultwise.dto.PaymentRequest;
import com.vaultwise.model.Payment;
import com.vaultwise.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public Payment createPayment(@RequestBody PaymentRequest paymentRequest) {
        return paymentService.createPayment(paymentRequest.getAccountNumber(), paymentRequest.getAmount(), paymentRequest.getDescription());
    }
}
