package com.vaultwise.controller;

import com.vaultwise.dto.BankingRequest;
import com.vaultwise.model.Transaction;
import com.vaultwise.service.BankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/banking")
public class BankingController {

    @Autowired
    private BankingService bankingService;

    @PostMapping("/deposit")
    public String deposit(@RequestBody BankingRequest request) {
        return bankingService.deposit(request.getAccountNumber(), request.getAmount());
    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestBody BankingRequest request) {
        return bankingService.withdraw(request.getAccountNumber(), request.getAmount());
    }
}
