package com.vaultwise.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;

    private String description; // Description of the payment (e.g., Bill payment, Loan payment)

    private Date paymentDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // Associated user making the payment

    // Constructors, Getters, Setters
    public Payment() {}

    public Payment(BigDecimal amount, String description, User user) {
        this.amount = amount;
        this.description = description;
        this.user = user;
        this.paymentDate = new Date();
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
