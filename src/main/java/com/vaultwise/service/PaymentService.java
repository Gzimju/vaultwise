package com.vaultwise.service;

import com.vaultwise.model.Account;
import com.vaultwise.model.Payment;
import com.vaultwise.repository.AccountRepository;
import com.vaultwise.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class PaymentService {

    private final AccountRepository accountRepository;
    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentService(AccountRepository accountRepository, PaymentRepository paymentRepository) {
        this.accountRepository = accountRepository;
        this.paymentRepository = paymentRepository;
    }

    public Payment createPayment(String accountNumber, BigDecimal amount, String description) {
        // Find the account by account number
        Optional<Account> account = accountRepository.findByAccountNumber(accountNumber);
        if (account.isEmpty()) {
            throw new RuntimeException("Account not found");
        }

        // Create a new Payment
        Payment payment = new Payment();
        payment.setAmount(amount);
        payment.setDescription(description);
        payment.setAccount(account.orElse(null));

        // Save the payment and return it
        return paymentRepository.save(payment);
    }
}
