package com.vaultwise.controller;

import com.vaultwise.model.Card;
import com.vaultwise.model.Payment;
import com.vaultwise.service.BankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/banking")
public class BankingController {

    private final BankingService bankingService;

    @Autowired
    public BankingController(BankingService bankingService) {
        this.bankingService = bankingService;
    }

    // Deposit Money into an Account
    @PostMapping("/deposit")
    public String deposit(@RequestParam String accountNumber, @RequestParam BigDecimal amount) {
        bankingService.deposit(accountNumber, amount);
        return "Deposit successful";
    }

    // Withdraw Money from an Account
    @PostMapping("/withdraw")
    public String withdraw(@RequestParam String accountNumber, @RequestParam BigDecimal amount) {
        bankingService.withdraw(accountNumber, amount);
        return "Withdrawal successful";
    }

    // Get Cards for an Account
    @GetMapping("/cards/{accountId}")
    public List<Card> getCards(@PathVariable Long accountId) {
        return bankingService.getCards(accountId);
    }

    // Get Payments for an Account
    @GetMapping("/payments/{accountId}")
    public List<Payment> getPayments(@PathVariable Long accountId) {
        return bankingService.getPayments(accountId);
    }
}
