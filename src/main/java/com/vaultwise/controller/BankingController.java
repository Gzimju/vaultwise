package com.vaultwise.controller;

import com.vaultwise.dto.BankingRequest;
import com.vaultwise.model.Account;
import com.vaultwise.model.Card;
import com.vaultwise.model.Payment;
import com.vaultwise.service.BankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    // Create Account
    @PostMapping("/create")
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        try {
            // Delegate the account creation to the service
            Account savedAccount = bankingService.createAccount(account);
            return ResponseEntity.ok(savedAccount);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Deposit Money into an Account
    @PostMapping("/deposit")
    public String deposit(@RequestBody BankingRequest bankingRequest) {
        bankingService.deposit(bankingRequest.getAccountNumber(), bankingRequest.getAmount());
        return "Deposit successful";
    }

    // Withdraw Money from an Account
    @PostMapping("/withdraw")
    public String withdraw(@RequestBody BankingRequest bankingRequest) {
        bankingService.withdraw(bankingRequest.getAccountNumber(), bankingRequest.getAmount());
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
