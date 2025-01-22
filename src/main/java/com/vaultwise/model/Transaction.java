package com.vaultwise.model;

import jakarta.persistence.*;

import java.util.Date;

import java.math.BigDecimal;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal amount;
    private Date transactionDate;
    private String type;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    public Transaction(Long id, BigDecimal amount, Date transactionDate, String type, Account account) {
        this.id = id;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.type = type;
        this.account = account;
    }
    public Transaction() {

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
    public Date getTransactionDate() {
        return transactionDate;
    }
    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public Account getAccount() {
        return account;
    }
    public void setAccount(Account account) {
        this.account = account;
    }
}
