package com.vaultwise.service;

import com.vaultwise.dto.PaymentRequest;
import com.vaultwise.model.Account;
import com.vaultwise.model.Payment;
import com.vaultwise.repository.AccountRepository;
import com.vaultwise.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
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
    // Get all payments
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    // Get a payment by ID
    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found with ID: " + id));
    }

    public Payment updatePayment(Long id, PaymentRequest paymentRequest) {
        // Check if payment exists
        Payment existingPayment = paymentRepository.findById(id).orElse(null);

        if (existingPayment != null) {
            // Update payment details
            existingPayment.setAmount(paymentRequest.getAmount());
            existingPayment.setDescription(paymentRequest.getDescription());
            // Optionally, you could also update the account if needed
            // existingPayment.setAccount(paymentRequest.getAccount());

            // Save and return the updated payment
            return paymentRepository.save(existingPayment);
        }

        return null; // Return null if payment not found
    }

    // Delete a payment by ID
    public void deletePayment(Long id) {
        if (!paymentRepository.existsById(id)) {
            throw new RuntimeException("Payment not found with ID: " + id);
        }
        paymentRepository.deleteById(id);
    }
}
