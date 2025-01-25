package com.vaultwise.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount",nullable = false)
    private BigDecimal amount;

    @Column(name = "type", nullable = false)
    private String type; // Deposit, Withdrawal, etc.

    @Column(name = "transaction_date", nullable = false)
    private Date transactionDate;

    @ManyToOne
    @JoinColumn(name = "account_id")
    @JsonIgnore
    private Account account; // Associated account for this transaction

    @Transient // This field will not be persisted to the database
    private Long accountId; // Temporary field to pass accountId in the request

    // Constructors, Getters, Setters
    public Transaction() {}

    public Transaction(BigDecimal amount, String type, Account account) {
        this.amount = amount;
        this.type = type;
        this.account = account;
        this.transactionDate = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

}
