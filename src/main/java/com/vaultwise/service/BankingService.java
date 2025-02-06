package com.vaultwise.service;

import com.vaultwise.model.Account;
import com.vaultwise.model.Card;
import com.vaultwise.model.Payment;
import com.vaultwise.model.Transaction;
import com.vaultwise.repository.AccountRepository;
import com.vaultwise.repository.CardRepository;
import com.vaultwise.repository.PaymentRepository;
import com.vaultwise.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BankingService {

    private final AccountRepository accountRepository;
    private final CardRepository cardRepository;
    private final PaymentRepository paymentRepository;
    private final TransactionService transactionService;  // Inject TransactionService

    @Autowired
    public BankingService(AccountRepository accountRepository, CardRepository cardRepository, PaymentRepository paymentRepository, TransactionService transactionService) {
        this.accountRepository = accountRepository;
        this.cardRepository = cardRepository;
        this.paymentRepository = paymentRepository;
        this.transactionService = transactionService;
    }

    public Account getAccountById(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with ID: " + accountId));
    }

    public Account createAccount(Account account) {
        if (account.getAccountNumber() == null || account.getAccountNumber().isEmpty()) {
            throw new IllegalArgumentException("Account number cannot be null or empty");
        }

        // Check if account number already exists
        if (accountRepository.findByAccountNumber(account.getAccountNumber()).isPresent()) {
            throw new IllegalArgumentException("Account number already exists");
        }

        if (account.getBalance() == null) {
            account.setBalance(BigDecimal.ZERO); // Default balance if not provided
        }

        return accountRepository.save(account);
    }

    // Deposit Money into an Account
    public void deposit(String accountNumber, BigDecimal amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        // Update account balance
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);

        // Create and save transaction
        Transaction transaction = new Transaction(amount, "Deposit", account);
        transactionService.createTransaction(transaction);  // Save the transaction
    }

    // Withdraw Money from an Account
    public void withdraw(String accountNumber, BigDecimal amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        // Update account balance
        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);

        // Create and save transaction
        Transaction transaction = new Transaction(amount, "Withdrawal", account);
        transactionService.createTransaction(transaction);  // Save the transaction
    }

    public Account updateAccount(Long accountId, Account updatedAccount) {
        return accountRepository.findById(accountId).map(existingAccount -> {
            if (updatedAccount.getAccountNumber() != null) {
                existingAccount.setAccountNumber(updatedAccount.getAccountNumber());
            }
            if (updatedAccount.getBalance() != null) {
                existingAccount.setBalance(updatedAccount.getBalance());
            }
            if (updatedAccount.getUser() != null) {
                existingAccount.setUser(updatedAccount.getUser());
            }
            return accountRepository.save(existingAccount);
        }).orElseThrow(() -> new RuntimeException("Account not found with ID: " + accountId));
    }

    public List<Card> getCards(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        return cardRepository.findByAccount(account);
    }

    public List<Payment> getPayments(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        return paymentRepository.findByAccount(account);
    }

    public Account getAccountByNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public void deleteAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        accountRepository.delete(account);
    }
}
