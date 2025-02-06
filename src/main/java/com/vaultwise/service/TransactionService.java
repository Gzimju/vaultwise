package com.vaultwise.service;

import com.vaultwise.model.Transaction;
import com.vaultwise.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

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

    // Update a transaction
    public Transaction updateTransaction(Long id, Transaction transaction) {
        Optional<Transaction> existingTransactionOpt = transactionRepository.findById(id);

        if (existingTransactionOpt.isPresent()) {
            Transaction existingTransaction = existingTransactionOpt.get();
            existingTransaction.setAmount(transaction.getAmount());
            existingTransaction.setType(transaction.getType());
            existingTransaction.setTransactionDate(transaction.getTransactionDate());
            return transactionRepository.save(existingTransaction);
        }

        return null; // If the transaction doesn't exist, return null
    }

    // Delete a transaction by ID
    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }
}
