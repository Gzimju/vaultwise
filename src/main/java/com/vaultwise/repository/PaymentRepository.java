package com.vaultwise.repository;

import com.vaultwise.model.Account;
import com.vaultwise.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByAccount(Account account);
}

