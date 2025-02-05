package com.vaultwise.service;

import com.vaultwise.model.Account;
import com.vaultwise.model.Transaction;
import com.vaultwise.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private BankingService bankingService; // Injecting the BankingService to fetch Account by ID

    // Create a new transaction
    public Transaction createTransaction(Transaction transaction) {
        if (transaction.getAccount() == null) {
            throw new IllegalArgumentException("Transaction must have an associated account");
        }
        return transactionRepository.save(transaction);
    }

    // Get all transactions
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    // Get a transaction by ID
    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found with ID: " + id));
    }

    // Delete a transaction by ID
    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }
}
