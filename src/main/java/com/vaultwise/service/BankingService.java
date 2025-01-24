package com.vaultwise.service;

import com.vaultwise.model.Account;
import com.vaultwise.model.Card;
import com.vaultwise.model.Payment;
import com.vaultwise.repository.AccountRepository;
import com.vaultwise.repository.CardRepository;
import com.vaultwise.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BankingService {

    private final AccountRepository accountRepository;
    private final CardRepository cardRepository;
    private final PaymentRepository paymentRepository;

    @Autowired
    public BankingService(AccountRepository accountRepository, CardRepository cardRepository, PaymentRepository paymentRepository) {
        this.accountRepository = accountRepository;
        this.cardRepository = cardRepository;
        this.paymentRepository = paymentRepository;
    }

    // Deposit Money into an Account
    public void deposit(String accountNumber, BigDecimal amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);
    }

    // Withdraw Money from an Account
    public void withdraw(String accountNumber, BigDecimal amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }
        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);
    }

    // Get Cards for an Account
    public List<Card> getCards(Long accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new RuntimeException("Account not found"));
        return cardRepository.findByAccount(account);
    }

    // Get Payments for an Account
    public List<Payment> getPayments(Long accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new RuntimeException("Account not found"));
        return paymentRepository.findByAccount(account);
    }
}
