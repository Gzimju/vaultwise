package com.vaultwise.service;

import com.vaultwise.model.Account;
import com.vaultwise.model.Card;
import com.vaultwise.model.Transaction;
import com.vaultwise.repository.AccountRepository;
import com.vaultwise.repository.CardRepository;
import com.vaultwise.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Random;

@Service
public class BankingService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final CardRepository cardRepository;

    @Autowired
    public BankingService(AccountRepository accountRepository, TransactionRepository transactionRepository, CardRepository cardRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.cardRepository = cardRepository;
    }

    // Deposit Method
    public String deposit(String accountNumber, BigDecimal amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            return "Account not found.";
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return "Deposit amount must be greater than zero.";
        }

        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setTransactionDate(new Date());
        transaction.setType("Deposit");
        transaction.setAccount(account);
        transactionRepository.save(transaction);

        return "Deposit successful. New Balance: " + account.getBalance();
    }

    // Withdraw Method
    public String withdraw(String accountNumber, BigDecimal amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            return "Account not found.";
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return "Withdrawal amount must be greater than zero.";
        }

        if (account.getBalance().compareTo(amount) < 0) {
            return "Insufficient funds.";
        }

        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setTransactionDate(new Date());
        transaction.setType("Withdrawal");
        transaction.setAccount(account);
        transactionRepository.save(transaction);

        return "Withdrawal successful. New Balance: " + account.getBalance();
    }

}