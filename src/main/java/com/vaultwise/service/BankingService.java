package com.vaultwise.service;

import com.vaultwise.model.*;
import com.vaultwise.repository.AccountRepository;
import com.vaultwise.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class BankingService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public String deposit(String accountNumber, BigDecimal amount ) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account != null) {
            account.setBalance(account.getBalance().add(amount));
            accountRepository.save(account);

            Transaction transaction = new Transaction();
            transaction.setAmount(amount);
            transaction.setTransactionDate(new Date());
            transaction.setType("Deposit");
            transaction.setAccount(account);
            transactionRepository.save(transaction);

            return "Deposit succsesful. New Balance: " + account.getBalance();
        }
        return "Account not found";
    }
    public String withdraw(String accountNumber, BigDecimal amount ) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account != null && account.getBalance().compareTo(amount) >= 0) {
            account.setBalance(account.getBalance().subtract(amount));
            accountRepository.save(account);

            Transaction transaction = new Transaction();
            transaction.setAmount(amount);
            transaction.setTransactionDate(new Date());
            transaction.setType("Withdrawal");
            transaction.setAccount(account);
            transactionRepository.save(transaction);

            return "Withdrawal succsesful. New Balance: " + account.getBalance();
        }
        return "Insufficient funds or account not found";
    }
}
